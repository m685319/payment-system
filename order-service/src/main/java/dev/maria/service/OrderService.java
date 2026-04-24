package dev.maria.service;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.entity.Order;
import dev.maria.entity.OrderStatus;
import dev.maria.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();
    private final OrderMapper orderMapper;

    public Order create(CreateOrderRequest request) {
        Order order = orderMapper.toEntity(request);

        UUID id = UUID.randomUUID();
        order.setId(id);
        order.setStatus(OrderStatus.NEW);

        storage.put(id, order);
        return order;
    }

    public Order getById(UUID id) {
        Order order = storage.get(id);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }
        return order;
    }

}
