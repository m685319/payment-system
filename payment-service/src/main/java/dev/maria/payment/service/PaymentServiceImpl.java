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
import org.springframework.dao.DataIntegrityViolationException;
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
    public ProcessPaymentResponse process(String key, ProcessPaymentRequest request) {
        Optional<PaymentRequestEntity> existing = repository.findById(key);
        if (existing.isPresent()) {
            PaymentRequestEntity entity = existing.get();
            return new ProcessPaymentResponse(entity.getPaymentId(), entity.getStatus());
        }
        log.debug("Calling OrderService, orderId={}", request.orderId());
        try {
            orderClient.getById(request.orderId());
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new OrderNotFoundException();
            }
            throw ex;
        }

        UUID paymentId = UUID.randomUUID();

        PaymentRequestEntity entity = new PaymentRequestEntity();
        entity.setIdempotencyKey(key);
        entity.setPaymentId(paymentId);
        entity.setStatus(PaymentStatus.SUCCESS);

        try {
            repository.save(entity);
            return new ProcessPaymentResponse(paymentId, PaymentStatus.SUCCESS);
        } catch (DataIntegrityViolationException ex) {
            PaymentRequestEntity exist = repository.findById(key).orElseThrow();
            return new ProcessPaymentResponse(exist.getPaymentId(), exist.getStatus());
        }
    }

    public ProcessPaymentResponse fallback(String key, ProcessPaymentRequest request, Throwable ex) {
        log.error("FALLBACK CALLED for orderId={}", request.orderId(), ex);
        throw new OrderServiceUnavailableException();
    }
}
