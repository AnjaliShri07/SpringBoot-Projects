package com.PaymentService.PaymentProcessing.repository;

import com.PaymentService.PaymentProcessing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
