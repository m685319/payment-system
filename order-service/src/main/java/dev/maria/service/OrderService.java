package dev.maria.service;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.dto.CreateOrderResponse;
import dev.maria.exception.OrderNotFoundException;

import java.util.UUID;

public interface OrderService {
    CreateOrderResponse create(CreateOrderRequest request);

    CreateOrderResponse getById(UUID id) throws OrderNotFoundException;
}