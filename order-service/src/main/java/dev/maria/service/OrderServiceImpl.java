package dev.maria.service;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.entity.Order;

import java.util.UUID;

public interface OrderServiceImpl {
    Order create(CreateOrderRequest request);

    Order getById(UUID id);
}