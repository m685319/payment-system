package dev.maria.payment.service;

import dev.maria.payment.client.NotificationClient;
import dev.maria.payment.client.OrderClient;
import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.NotificationRequest;
import dev.maria.payment.entity.PaymentRequestEntity;
import dev.maria.payment.repository.PaymentRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentAsyncProcessor {

    private final OrderClient orderClient;
    private final PaymentRequestRepository repository;
    private final NotificationClient notificationClient;

    @Async
    //@CircuitBreaker(name = "orderClient", fallbackMethod = "fallback")
    //@Retry(name = "orderClient")
    public void processAsync(String idempotencyKey, UUID paymentId, UUID orderId) {
        log.info("ASYNC start, paymentId={}, key={}", paymentId, idempotencyKey);

        try {
            orderClient.getById(orderId);

            updateStatus(idempotencyKey, PaymentStatus.SUCCESS);
            log.info("ASYNC success, paymentId={}", paymentId);

            sendNotification("Payment SUCCESS: " + paymentId);

        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                updateStatus(idempotencyKey, PaymentStatus.FAILED);
                log.warn("ASYNC order not found, paymentId={}", paymentId);
                sendNotification("Payment FAILED: " + paymentId);
                return;
            }

            updateStatus(idempotencyKey, PaymentStatus.FAILED);
            log.error("ASYNC error, paymentId={}", paymentId, ex);
            sendNotification("Payment FAILED: " + paymentId);
        } catch (ResourceAccessException ex) {
            updateStatus(idempotencyKey, PaymentStatus.FAILED);
            log.error("ASYNC transport error while calling order-service, paymentId={}", paymentId, ex);
            sendNotification("Payment FAILED: " + paymentId);
        } catch (Exception ex) {
            updateStatus(idempotencyKey, PaymentStatus.FAILED);
            log.error("ASYNC unexpected error, paymentId={}", paymentId, ex);
            sendNotification("Payment FAILED: " + paymentId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatus(String key, PaymentStatus status){
        PaymentRequestEntity entity = repository.findById(key).orElseThrow();
        entity.setStatus(status);
        repository.save(entity);
    }

    public void fallback(String key, UUID paymentId, UUID orderId, Throwable ex) {

        log.error("Fallback triggered, paymentId={}, key={}", paymentId, key, ex);

        updateStatus(key, PaymentStatus.FAILED);
    }

    private void sendNotification(String message) {
        try {
            notificationClient.send(new NotificationRequest("test@mail.com", message));
            log.info("Notification sent: {}", message);
        } catch (Exception ex) {
            log.error("Failed to send notification: {}", message, ex);
        }
    }
}
