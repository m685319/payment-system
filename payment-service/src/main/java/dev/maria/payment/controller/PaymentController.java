package dev.maria.payment.controller;

import dev.maria.payment.domain.PaymentStatus;
import dev.maria.payment.dto.ProcessPaymentRequest;
import dev.maria.payment.dto.ProcessPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    @PostMapping
    public ResponseEntity<ProcessPaymentResponse> process(@RequestBody ProcessPaymentRequest request) {
        ProcessPaymentResponse response = new ProcessPaymentResponse(UUID.randomUUID(), PaymentStatus.SUCCESS);
        return ResponseEntity.ok(response);
    }
}
