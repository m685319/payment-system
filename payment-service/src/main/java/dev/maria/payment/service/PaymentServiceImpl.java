package dev.maria.payment.service;

import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Override
    public ProcessPaymentResponse process(ProcessPaymentRequest request) {

        return new ProcessPaymentResponse(UUID.randomUUID(), PaymentStatus.SUCCESS);
    }
}
