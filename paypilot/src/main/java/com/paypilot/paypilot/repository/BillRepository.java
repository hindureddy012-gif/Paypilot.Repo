package com.paypilot.paypilot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paypilot.paypilot.entity.Bill;
import com.paypilot.paypilot.entity.BillStatus;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByStatus(BillStatus status);
}