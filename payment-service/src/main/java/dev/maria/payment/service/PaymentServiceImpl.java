package dev.maria.payment.service;

import dev.maria.payment.client.OrderClient;
import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderClient orderClient;

    @Override
    public ProcessPaymentResponse process(ProcessPaymentRequest request) {
        orderClient.getById(request.orderId());
        return new ProcessPaymentResponse(UUID.randomUUID(), PaymentStatus.SUCCESS);
    }
}
