package com.paypilot.paypilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.paypilot.paypilot.entity.Bill;
import com.paypilot.paypilot.entity.BillStatus;
import com.paypilot.paypilot.repository.BillRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    private static final Logger logger =
            LoggerFactory.getLogger(BillService.class);

    private final BillRepository billRepository;

    public BillService(BillRepository billRepository) {
        this.billRepository = billRepository;

        logger.info("BillService initialized successfully");
    }

    public Bill createBill(Bill bill) {

        logger.info("Creating new bill: {}", bill.getBillName());

        if (bill.getStatus() == null) {
            bill.setStatus(BillStatus.UPCOMING);

            logger.info(
                    "Bill status was null. Setting default status to UPCOMING"
            );
        }

        Bill savedBill = billRepository.save(bill);

        logger.info(
                "Bill created successfully with ID: {}",
                savedBill.getId()
        );

        return savedBill;
    }

    public List<Bill> getAllBills() {

        logger.info("Fetching all bills");

        List<Bill> bills = billRepository.findAll();

        logger.info(
                "Total bills found: {}",
                bills.size()
        );

        return bills;
    }

    public Optional<Bill> getBillById(Long id) {

        logger.info("Fetching bill with ID: {}", id);

        Optional<Bill> bill = billRepository.findById(id);

        if (bill.isPresent()) {
            logger.info(
                    "Bill found with ID: {}",
                    id
            );
        } else {
            logger.warn(
                    "Bill not found with ID: {}",
                    id
            );
        }

        return bill;
    }

    public Optional<Bill> updateBill(
            Long id,
            Bill updatedBill) {

        logger.info(
                "Updating bill with ID: {}",
                id
        );

        return billRepository.findById(id)
                .map(existingBill -> {

                    existingBill.setBillName(
                            updatedBill.getBillName()
                    );

                    existingBill.setAmount(
                            updatedBill.getAmount()
                    );

                    existingBill.setDueDate(
                            updatedBill.getDueDate()
                    );

                    existingBill.setStatus(
                            updatedBill.getStatus()
                    );

                    existingBill.setPaymentMethod(
                            updatedBill.getPaymentMethod()
                    );

                    Bill savedBill =
                            billRepository.save(existingBill);

                    logger.info(
                            "Bill updated successfully with ID: {}",
                            id
                    );

                    return savedBill;
                });
    }

    public boolean deleteBill(Long id) {

        logger.info(
                "Deleting bill with ID: {}",
                id
        );

        if (!billRepository.existsById(id)) {

            logger.warn(
                    "Cannot delete. Bill not found with ID: {}",
                    id
            );

            return false;
        }

        billRepository.deleteById(id);

        logger.info(
                "Bill deleted successfully with ID: {}",
                id
        );

        return true;
    }

    public List<Bill> getUpcomingBills() {

        logger.info("Fetching upcoming bills");

        List<Bill> bills =
                billRepository.findByStatus(
                        BillStatus.UPCOMING
                );

        logger.info(
                "Upcoming bills found: {}",
                bills.size()
        );

        return bills;
    }

    public List<Bill> getOverdueBills() {

        logger.info("Fetching overdue bills");

        List<Bill> bills =
                billRepository.findByStatus(
                        BillStatus.OVERDUE
                );

        logger.info(
                "Overdue bills found: {}",
                bills.size()
        );

        return bills;
    }

    public List<Bill> getPaidBills() {

        logger.info("Fetching paid bills");

        List<Bill> bills =
                billRepository.findByStatus(
                        BillStatus.PAID
                );

        logger.info(
                "Paid bills found: {}",
                bills.size()
        );

        return bills;
    }

    public List<Bill> getCancelledBills() {

        logger.info("Fetching cancelled bills");

        List<Bill> bills =
                billRepository.findByStatus(
                        BillStatus.CANCELLED
                );

        logger.info(
                "Cancelled bills found: {}",
                bills.size()
        );

        return bills;
    }
}