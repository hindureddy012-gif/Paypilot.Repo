
package com.paypilot.repository;

import com.paypilot.entity.ScheduledPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledPaymentRepository
        extends JpaRepository<ScheduledPayment, Integer> {
}