package dev.maria.payment.service;

import dev.maria.payment.client.OrderClient;
import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import dev.maria.payment.entity.PaymentRequestEntity;
import dev.maria.payment.exception.OrderNotFoundException;
import dev.maria.payment.exception.OrderServiceUnavailableException;
import dev.maria.payment.repository.PaymentRequestRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderClient orderClient;
    private final PaymentRequestRepository repository;

    @Override
    @CircuitBreaker(name = "orderClient", fallbackMethod = "fallback")
    @Retry(name = "orderClient")
    public ProcessPaymentResponse process(String idempotencyKey, ProcessPaymentRequest request) {

        Optional<PaymentRequestEntity> existing = repository.findById(idempotencyKey);

        if (existing.isPresent()) {
            PaymentRequestEntity entity = existing.get();
            log.info("Idempotent request detected: idempotencyKey={}, existing paymentId={}, status={}", idempotencyKey, entity.getPaymentId(), entity.getStatus());
            return new ProcessPaymentResponse(entity.getPaymentId(), entity.getStatus());
        }

        UUID paymentId = UUID.randomUUID();
        log.info("Creating payment, paymentId={}, idempotencyKey={}", paymentId, idempotencyKey);
        PaymentRequestEntity entity = new PaymentRequestEntity();
        entity.setIdempotencyKey(idempotencyKey);
        entity.setPaymentId(paymentId);
        entity.setStatus(PaymentStatus.PROCESSING);
        log.debug("Saving payment: paymentId={}, idempotencyKey={}", paymentId, idempotencyKey);
        repository.save(entity);
        log.info("Payment saved, status={}, paymentId={}, idempotencyKey={}", entity.getStatus(), paymentId, idempotencyKey);
        log.debug("Calling OrderService, orderId={}, paymentId={}, idempotencyKey={}", request.orderId(), paymentId, idempotencyKey);

        try {
            orderClient.getById(request.orderId());
            entity.setStatus(PaymentStatus.SUCCESS);
            repository.save(entity);
            log.info("Payment completed, status={}, paymentId={}, idempotencyKey={}", entity.getStatus(), paymentId, idempotencyKey);
            return new ProcessPaymentResponse(paymentId, PaymentStatus.SUCCESS);
        } catch (RestClientResponseException ex) {
            entity.setStatus(PaymentStatus.FAILED);
            repository.save(entity);
            log.error("Payment failed, status={}, paymentId={}, idempotencyKey={}, httpStatus={}", entity.getStatus(), paymentId, idempotencyKey, ex.getStatusCode(), ex);
            if (ex.getStatusCode().value() == 404) {
                throw new OrderNotFoundException();
            }
            throw ex;
        }
    }

    public ProcessPaymentResponse fallback(String idempotencyKey, ProcessPaymentRequest request, Throwable ex) {
        log.error("Fallback triggered, orderId={}, idempotencyKey={}", request.orderId(), idempotencyKey, ex);
        throw new OrderServiceUnavailableException();
    }
}
