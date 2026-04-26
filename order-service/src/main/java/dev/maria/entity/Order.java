package dev.maria.entity;

import dev.maria.domain.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @GeneratedValue
    @Id
    private UUID id;

    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
