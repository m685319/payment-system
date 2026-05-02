package dev.maria.payment.dto;

import dev.maria.payment.domain.PaymentStatus;

import java.util.UUID;

public record PaymentStatusResponse(UUID paymentId, PaymentStatus status) {}