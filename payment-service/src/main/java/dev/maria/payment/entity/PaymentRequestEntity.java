package dev.maria.payment.entity;

import dev.maria.payment.domain.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "payment_requests")
@Getter
@Setter
public class PaymentRequestEntity {

    @Id
    @Column(unique = true)
    private String idempotencyKey;

    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}