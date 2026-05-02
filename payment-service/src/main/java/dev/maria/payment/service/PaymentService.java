package dev.maria.payment.service;

import dev.maria.payment.dto.PaymentStatusResponse;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;

public interface PaymentService {

    ProcessPaymentResponse process(String idempotencyKey, ProcessPaymentRequest request);

    PaymentStatusResponse getStatus(String idempotencyKey);
}
