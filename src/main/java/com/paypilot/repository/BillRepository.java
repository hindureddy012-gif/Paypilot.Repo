package com.paypilot.repository;

import com.paypilot.entity.Bill;
import com.paypilot.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByUserId(Long userId);

    List<Bill> findByUserIdAndStatus(
            Long userId,
            BillStatus status
    );

    List<Bill> findByUserIdAndDueDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );
}