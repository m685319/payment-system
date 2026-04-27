package dev.maria.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentRequest(UUID orderId, BigDecimal amount, String currency) {}