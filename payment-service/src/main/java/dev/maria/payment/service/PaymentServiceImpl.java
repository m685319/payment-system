package dev.maria.payment.service;

import dev.maria.payment.client.OrderClient;
import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import dev.maria.payment.exception.OrderServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderClient orderClient;

    @Override
    @CircuitBreaker(name = "orderClient", fallbackMethod = "fallback")
    @Retry(name = "orderClient")
    public ProcessPaymentResponse process(ProcessPaymentRequest request) {
            log.debug("Calling OrderService, orderId={}", request.orderId());
            orderClient.getById(request.orderId());
        return new ProcessPaymentResponse(UUID.randomUUID(), PaymentStatus.SUCCESS);
    }

    public ProcessPaymentResponse fallback(ProcessPaymentRequest request, Throwable ex) {
        throw new OrderServiceUnavailableException();
    }
}
