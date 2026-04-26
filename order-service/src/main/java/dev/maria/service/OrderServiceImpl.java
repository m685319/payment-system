package dev.maria.service;

import dev.maria.domain.OrderStatus;
import dev.maria.dto.CreateOrderRequest;
import dev.maria.dto.CreateOrderResponse;
import dev.maria.entity.Order;
import dev.maria.exception.OrderNotFoundException;
import dev.maria.mapper.OrderMapper;
import dev.maria.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public CreateOrderResponse create(CreateOrderRequest request) {
        Order order = orderMapper.toEntity(request);
        order.setStatus(OrderStatus.NEW);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public CreateOrderResponse getById(UUID id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return orderMapper.toResponse(order);
    }
}