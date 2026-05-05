package dev.maria.payment.repository;

import dev.maria.payment.entity.PaymentRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequestEntity, String> {

    Optional<PaymentRequestEntity> findByOrderId(UUID orderId);
}