package dev.maria.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(BigDecimal amount, String currency) {}
