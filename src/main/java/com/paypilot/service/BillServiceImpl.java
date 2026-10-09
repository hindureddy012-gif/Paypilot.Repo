package com.paypilot.service;

import com.paypilot.dto.BillRequest;
import com.paypilot.entity.Bill;
import com.paypilot.entity.BillStatus;
import com.paypilot.exception.BillNotFoundException;
import com.paypilot.repository.BillRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger logger =
            LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;

    public BillServiceImpl(BillRepository billRepository) {
        this.billRepository = billRepository;
    }
 
    @Override
    public Bill addBill(BillRequest request) {

        logger.info("Adding new bill: {}", request.getBillName());

        Bill bill = new Bill();

        bill.setUserId(request.getUserId());
        bill.setBillName(request.getBillName());
        bill.setBillingCompany(request.getBillingCompany());
        bill.setAmount(request.getAmount());
        bill.setDueDate(request.getDueDate());
        bill.setCategory(request.getCategory());

        bill.setRecurring(request.getRecurring());
        bill.setRecurrenceType(request.getRecurrenceType());

        bill.setReminderEnabled(request.getReminderEnabled());
        bill.setReminderDaysBefore(request.getReminderDaysBefore());

        bill.setStatus(BillStatus.UPCOMING);

        Bill savedBill = billRepository.save(bill);

        logger.info(
                "Bill added successfully. Bill ID: {}",
                savedBill.getId()
        );

        return savedBill;
    }

    @Override
    public List<Bill> getAllBills() {

        logger.info("Fetching all bills");

        List<Bill> bills = billRepository.findAll();

        logger.info(
                "Total bills retrieved: {}",
                bills.size()
        );

        return bills;
    }

    @Override
    public List<Bill> getBillsByUser(Long userId) {

        logger.info(
                "Fetching bills for user ID: {}",
                userId
        );

        List<Bill> bills =
                billRepository.findByUserId(userId);

        logger.info(
                "Retrieved {} bills for user ID: {}",
                bills.size(),
                userId
        );

        return bills;
    }

    @Override
    public Bill getBillById(Long id) {

        logger.info(
                "Fetching bill with ID: {}",
                id
        );

        return billRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn(
                            "Bill not found with ID: {}",
                            id
                    );

                    return new BillNotFoundException(id);
                });
    }

    @Override
    public Bill updateBill(
            Long id,
            BillRequest request) {

        logger.info(
                "Updating bill with ID: {}",
                id
        );

        Bill bill = getBillById(id);

        bill.setUserId(request.getUserId());
        bill.setBillName(request.getBillName());
        bill.setBillingCompany(request.getBillingCompany());
        bill.setAmount(request.getAmount());
        bill.setDueDate(request.getDueDate());
        bill.setCategory(request.getCategory());

        bill.setRecurring(request.getRecurring());
        bill.setRecurrenceType(request.getRecurrenceType());

        bill.setReminderEnabled(
                request.getReminderEnabled()
        );

        bill.setReminderDaysBefore(
                request.getReminderDaysBefore()
        );

        Bill updatedBill =
                billRepository.save(bill);

        logger.info(
                "Bill updated successfully. Bill ID: {}",
                id
        );

        return updatedBill;
    }

    @Override
    public void deleteBill(Long id) {

        logger.info(
                "Deleting bill with ID: {}",
                id
        );

        Bill bill = getBillById(id);

        billRepository.delete(bill);

        logger.info(
                "Bill deleted successfully. Bill ID: {}",
                id
        );
    }

    @Override
    public List<Bill> getUpcomingBills(Long userId) {

        logger.info(
                "Fetching upcoming bills for user ID: {}",
                userId
        );

        LocalDate today = LocalDate.now();

        LocalDate nextThirtyDays =
                today.plusDays(30);

        List<Bill> bills =
                billRepository.findByUserIdAndDueDateBetween(
                        userId,
                        today,
                        nextThirtyDays
                );

        logger.info(
                "Upcoming bills retrieved: {}",
                bills.size()
        );

        return bills;
    }

    @Override
    public List<Bill> getOverdueBills(Long userId) {

        logger.info(
                "Fetching overdue bills for user ID: {}",
                userId
        );

        List<Bill> bills =
                billRepository.findByUserIdAndStatus(
                        userId,
                        BillStatus.OVERDUE
                );

        logger.info(
                "Overdue bills retrieved: {}",
                bills.size()
        );

        return bills;
    }

    @Override
    public Bill markAsPaid(Long id) {

        logger.info(
                "Marking bill as paid. Bill ID: {}",
                id
        );

        Bill bill = getBillById(id);

        bill.setStatus(BillStatus.PAID);

        Bill savedBill =
                billRepository.save(bill);

        logger.info(
                "Bill marked as PAID. Bill ID: {}",
                id
        );

        return savedBill;
    }

    @Override
    public Bill snoozeBill(
            Long id,
            int days) {

        logger.info(
                "Snoozing bill ID: {} for {} days",
                id,
                days
        );

        Bill bill = getBillById(id);

        LocalDate snoozeDate =
                LocalDate.now().plusDays(days);

        bill.setSnoozedUntil(snoozeDate);

        Bill savedBill =
                billRepository.save(bill);

        logger.info(
                "Bill snoozed until {}. Bill ID: {}",
                snoozeDate,
                id
        );

        return savedBill;
    }

    @Override
    public Bill updateReminderSettings(
            Long id,
            Boolean enabled,
            Integer daysBefore) {

        logger.info(
                "Updating reminder settings for bill ID: {}",
                id
        );

        Bill bill = getBillById(id);

        bill.setReminderEnabled(enabled);
        bill.setReminderDaysBefore(daysBefore);

        Bill savedBill =
                billRepository.save(bill);

        logger.info(
                "Reminder settings updated for bill ID: {}",
                id
        );

        return savedBill;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateOverdueBills() {

        logger.info(
                "Starting scheduled overdue bill check"
        );

        LocalDate today = LocalDate.now();

        List<Bill> bills =
                billRepository.findAll();

        for (Bill bill : bills) {

            if (bill.getDueDate() != null
                    && bill.getDueDate().isBefore(today)
                    && bill.getStatus() != BillStatus.PAID) {

                bill.setStatus(BillStatus.OVERDUE);

                billRepository.save(bill);

                logger.info(
                        "Bill ID {} marked as OVERDUE",
                        bill.getId()
                );
            }
        }

        logger.info(
                "Scheduled overdue bill check completed"
        );
    }
}