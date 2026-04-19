package dev.maria.service;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.dto.CreateOrderResponse;
import dev.maria.entity.Order;
import dev.maria.entity.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    public CreateOrderResponse create(CreateOrderRequest request) {
        UUID id = UUID.randomUUID();

        Order order = new Order();
        order.setId(id);
        order.setAmount(request.amount());
        order.setCurrency(request.currency());
        order.setStatus(OrderStatus.NEW);

        storage.put(id, order);

        return new CreateOrderResponse(id, OrderStatus.NEW.name());
    }
}
