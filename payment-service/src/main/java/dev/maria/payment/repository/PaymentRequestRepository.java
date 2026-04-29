package dev.maria.payment.repository;

import dev.maria.payment.entity.PaymentRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequestEntity, String> { }