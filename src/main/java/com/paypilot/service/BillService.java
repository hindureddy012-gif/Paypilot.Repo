package com.paypilot.service;

import com.paypilot.dto.BillRequest;
import com.paypilot.entity.Bill;

import java.util.List;

public interface BillService {

    Bill addBill(BillRequest request);

    List<Bill> getAllBills();

    List<Bill> getBillsByUser(Long userId);

    Bill getBillById(Long id);

    Bill updateBill(Long id, BillRequest request);

    void deleteBill(Long id);

    List<Bill> getUpcomingBills(Long userId);

    List<Bill> getOverdueBills(Long userId);

    Bill markAsPaid(Long id);

    Bill snoozeBill(Long id, int days);

    Bill updateReminderSettings(
            Long id,
            Boolean enabled,
            Integer daysBefore
    );
}