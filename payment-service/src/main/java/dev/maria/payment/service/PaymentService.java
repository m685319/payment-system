package dev.maria.payment.service;

import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;

public interface PaymentService {

    ProcessPaymentResponse process(ProcessPaymentRequest request);
}
