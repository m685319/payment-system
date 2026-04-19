package dev.maria.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Order {
    private UUID id;
    private BigDecimal amount;
    private String currency;
    private OrderStatus status;
}
