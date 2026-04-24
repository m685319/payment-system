package dev.maria.dto;

import dev.maria.entity.OrderStatus;

import java.util.UUID;

public record CreateOrderResponse(UUID orderId, OrderStatus status) {}
