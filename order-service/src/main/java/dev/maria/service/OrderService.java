package dev.maria.service;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.entity.Order;
import dev.maria.entity.OrderStatus;
import dev.maria.exception.OrderNotFoundException;
import dev.maria.mapper.OrderMapper;
import dev.maria.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public Order create(CreateOrderRequest request) {
        Order order = orderMapper.toEntity(request);

        UUID id = UUID.randomUUID();
        order.setId(id);
        order.setStatus(OrderStatus.NEW);

        return orderRepository.save(order);
    }

    public Order getById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
