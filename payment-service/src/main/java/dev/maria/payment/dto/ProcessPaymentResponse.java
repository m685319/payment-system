package dev.maria.payment.dto;

import dev.maria.payment.domain.PaymentStatus;

import java.util.UUID;

public record ProcessPaymentResponse(UUID paymentId, PaymentStatus status) {}