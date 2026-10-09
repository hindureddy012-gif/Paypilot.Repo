package com.paypilot.paypilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypilot.paypilot.entity.Payment;
import com.paypilot.paypilot.entity.PaymentStatus;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByBillId(Long billId);

    List<Payment> findByStatus(PaymentStatus status);

    long countByStatus(PaymentStatus status);
}