package dev.maria.payment.client;

import dev.maria.payment.client.dto.CreateOrderResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange("/orders")
public interface OrderClient {

    @GetExchange("/{id}")
    CreateOrderResponse getById(@PathVariable("id") UUID id);
}