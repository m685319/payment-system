package dev.maria.payment.client.dto;

import java.util.UUID;

public record CreateOrderResponse(UUID orderId, OrderStatus status) {}
