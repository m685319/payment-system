package dev.maria.payment.service;

import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.PaymentStatusResponse;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import dev.maria.payment.entity.PaymentRequestEntity;
import dev.maria.payment.repository.PaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRequestRepository repository;
    private final PaymentAsyncProcessor asyncProcessor;

    @Override
    public ProcessPaymentResponse process(String idempotencyKey, ProcessPaymentRequest request) {

        var existing = repository.findById(idempotencyKey);
        if (existing.isPresent()) {
            var e = existing.get();
            log.info("Idempotent request: key={}, status={}", idempotencyKey, e.getStatus());
            return new ProcessPaymentResponse(e.getPaymentId(), e.getStatus(), true);
        }

        var existingByOrder = repository.findByOrderId(request.orderId());
        if (existingByOrder.isPresent()) {
            var e = existingByOrder.get();
            return new ProcessPaymentResponse(e.getPaymentId(), e.getStatus(),true);
        }

        UUID paymentId = UUID.randomUUID();

        PaymentRequestEntity entity = new PaymentRequestEntity();
        entity.setIdempotencyKey(idempotencyKey);
        entity.setPaymentId(paymentId);
        entity.setOrderId(request.orderId());
        entity.setStatus(PaymentStatus.PROCESSING);

        try {
            repository.save(entity);
        } catch (DataIntegrityViolationException ex) {
            var exist = repository.findById(idempotencyKey).orElseThrow();
            return new ProcessPaymentResponse(exist.getPaymentId(), exist.getStatus(), true);
        }

        log.info("Payment created: paymentId={}, key={}, status=PROCESSING", paymentId, idempotencyKey);

        asyncProcessor.processAsync(idempotencyKey, paymentId, request.orderId());

        return new ProcessPaymentResponse(paymentId, entity.getStatus(), false);
    }

    public PaymentStatusResponse getStatus(String idempotencyKey) {
        PaymentRequestEntity entity = repository.findById(idempotencyKey)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        return new PaymentStatusResponse(entity.getPaymentId(), entity.getStatus());
    }
}
