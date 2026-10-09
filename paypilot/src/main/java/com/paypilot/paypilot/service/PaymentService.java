package com.paypilot.paypilot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.paypilot.paypilot.entity.Bill;
import com.paypilot.paypilot.entity.BillStatus;
import com.paypilot.paypilot.entity.Payment;
import com.paypilot.paypilot.entity.PaymentStatus;
import com.paypilot.paypilot.repository.BillRepository;
import com.paypilot.paypilot.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final BillRepository billRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            BillRepository billRepository) {

        this.paymentRepository = paymentRepository;
        this.billRepository = billRepository;

        logger.info("PaymentService initialized successfully");
    }

    public Optional<Payment> createPayment(
            Long billId,
            Payment payment) {

        logger.info(
                "Creating payment for Bill ID: {}",
                billId
        );

        Optional<Bill> billOptional =
                billRepository.findById(billId);

        if (billOptional.isEmpty()) {

            logger.warn(
                    "Cannot create payment. Bill not found with ID: {}",
                    billId
            );

            return Optional.empty();
        }

        Bill bill = billOptional.get();

        payment.setBill(bill);

        if (payment.getStatus() == null) {

            payment.setStatus(
                    PaymentStatus.PENDING
            );

            logger.info(
                    "Payment status was null. Setting default status to PENDING"
            );
        }

        Payment savedPayment =
                paymentRepository.save(payment);

        logger.info(
                "Payment created successfully with ID: {}",
                savedPayment.getId()
        );

        if (payment.getStatus() == PaymentStatus.SUCCESS) {

            bill.setStatus(BillStatus.PAID);

            billRepository.save(bill);

            logger.info(
                    "Bill ID: {} marked as PAID",
                    billId
            );
        }

        return Optional.of(savedPayment);
    }

    public List<Payment> getAllPayments() {

        logger.info("Fetching all payments");

        List<Payment> payments =
                paymentRepository.findAll();

        logger.info(
                "Total payments found: {}",
                payments.size()
        );

        return payments;
    }

    public Optional<Payment> getPaymentById(Long id) {

        logger.info(
                "Fetching payment with ID: {}",
                id
        );

        Optional<Payment> payment =
                paymentRepository.findById(id);

        if (payment.isPresent()) {

            logger.info(
                    "Payment found with ID: {}",
                    id
            );

        } else {

            logger.warn(
                    "Payment not found with ID: {}",
                    id
            );
        }

        return payment;
    }

    public List<Payment> getPaymentsByBillId(
            Long billId) {

        logger.info(
                "Fetching payments for Bill ID: {}",
                billId
        );

        List<Payment> payments =
                paymentRepository.findByBillId(billId);

        logger.info(
                "Payments found for Bill ID {}: {}",
                billId,
                payments.size()
        );

        return payments;
    }

    public Optional<Payment> updatePayment(
            Long id,
            Long billId,
            Payment updatedPayment) {

        logger.info(
                "Updating Payment ID: {} for Bill ID: {}",
                id,
                billId
        );

        Optional<Payment> paymentOptional =
                paymentRepository.findById(id);

        Optional<Bill> billOptional =
                billRepository.findById(billId);

        if (paymentOptional.isEmpty()) {

            logger.warn(
                    "Payment not found with ID: {}",
                    id
            );

            return Optional.empty();
        }

        if (billOptional.isEmpty()) {

            logger.warn(
                    "Bill not found with ID: {}",
                    billId
            );

            return Optional.empty();
        }

        Payment existingPayment =
                paymentOptional.get();

        Bill bill =
                billOptional.get();

        existingPayment.setBill(bill);

        existingPayment.setAmount(
                updatedPayment.getAmount()
        );

        existingPayment.setPaymentDate(
                updatedPayment.getPaymentDate()
        );

        existingPayment.setStatus(
                updatedPayment.getStatus()
        );

        existingPayment.setPaymentMethod(
                updatedPayment.getPaymentMethod()
        );

        Payment savedPayment =
                paymentRepository.save(existingPayment);

        logger.info(
                "Payment updated successfully with ID: {}",
                id
        );

        if (existingPayment.getStatus()
                == PaymentStatus.SUCCESS) {

            bill.setStatus(BillStatus.PAID);

            billRepository.save(bill);

            logger.info(
                    "Bill ID: {} marked as PAID after payment update",
                    billId
            );
        }

        return Optional.of(savedPayment);
    }

    public boolean deletePayment(Long id) {

        logger.info(
                "Deleting payment with ID: {}",
                id
        );

        if (!paymentRepository.existsById(id)) {

            logger.warn(
                    "Cannot delete. Payment not found with ID: {}",
                    id
            );

            return false;
        }

        paymentRepository.deleteById(id);

        logger.info(
                "Payment deleted successfully with ID: {}",
                id
        );

        return true;
    }

    public Map<String, Object> getPaymentSummary() {

        logger.info("Generating payment summary");

        List<Payment> payments =
                paymentRepository.findAll();

        long totalPayments =
                payments.size();

        long successfulPayments =
                paymentRepository.countByStatus(
                        PaymentStatus.SUCCESS
                );

        long pendingPayments =
                paymentRepository.countByStatus(
                        PaymentStatus.PENDING
                );

        long failedPayments =
                paymentRepository.countByStatus(
                        PaymentStatus.FAILED
                );

        BigDecimal totalAmount =
                payments.stream()
                        .map(Payment::getAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal paidAmount =
                payments.stream()
                        .filter(payment ->
                                payment.getStatus()
                                        == PaymentStatus.SUCCESS
                        )
                        .map(Payment::getAmount)
                        .filter(amount -> amount != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        Map<String, Object> summary =
                new HashMap<>();

        summary.put(
                "totalPayments",
                totalPayments
        );

        summary.put(
                "successfulPayments",
                successfulPayments
        );

        summary.put(
                "pendingPayments",
                pendingPayments
        );

        summary.put(
                "failedPayments",
                failedPayments
        );

        summary.put(
                "totalAmount",
                totalAmount
        );
 
        summary.put(
                "paidAmount",
                paidAmount
        );

        logger.info(
                "Payment summary generated successfully. Total payments: {}",
                totalPayments
        );

        return summary;
    }
}