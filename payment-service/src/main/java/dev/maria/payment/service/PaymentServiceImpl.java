package dev.maria.payment.service;

import dev.maria.payment.client.OrderClient;
import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import dev.maria.payment.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderClient orderClient;

    @Override
    public ProcessPaymentResponse process(ProcessPaymentRequest request) {
        try {
            orderClient.getById(request.orderId());
        } catch (HttpClientErrorException.NotFound ex) {
            throw new OrderNotFoundException();
        }
        return new ProcessPaymentResponse(UUID.randomUUID(), PaymentStatus.SUCCESS);
    }
}
