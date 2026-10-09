package com.paypilot.paypilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypilot.paypilot.entity.Payment;
import com.paypilot.paypilot.service.PaymentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private static final Logger logger =
            LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;

        logger.info(
                "PaymentController initialized successfully"
        );
    }

    @PostMapping("/bill/{billId}")
    public ResponseEntity<?> createPayment(
            @PathVariable Long billId,
            @RequestBody Payment payment) {

        logger.info(
                "POST /api/payments/bill/{} - Creating payment",
                billId
        );

        var result =
                paymentService.createPayment(
                        billId,
                        payment
                );

        if (result.isEmpty()) {

            logger.warn(
                    "Cannot create payment. Bill not found with ID: {}",
                    billId
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Bill not found with id: "
                                    + billId
                    );
        }

        logger.info(
                "Payment created successfully for Bill ID: {}",
                billId
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result.get());
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {

        logger.info(
                "GET /api/payments - Fetching all payments"
        );

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(
            @PathVariable Long id) {

        logger.info(
                "GET /api/payments/{} - Fetching payment",
                id
        );

        var result =
                paymentService.getPaymentById(id);

        if (result.isEmpty()) {

            logger.warn(
                    "Payment not found with ID: {}",
                    id
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Payment not found with id: "
                                    + id
                    );
        }

        logger.info(
                "Payment found with ID: {}",
                id
        );

        return ResponseEntity.ok(
                result.get()
        );
    }

    @GetMapping("/bill/{billId}")
    public ResponseEntity<List<Payment>>
    getPaymentsByBillId(
            @PathVariable Long billId) {

        logger.info(
                "GET /api/payments/bill/{} - Fetching payments",
                billId
        );

        return ResponseEntity.ok(
                paymentService.getPaymentsByBillId(
                        billId
                )
        );
    }

    @PutMapping("/{id}/bill/{billId}")
    public ResponseEntity<?> updatePayment(
            @PathVariable Long id,
            @PathVariable Long billId,
            @RequestBody Payment payment) {

        logger.info(
                "PUT /api/payments/{}/bill/{} - Updating payment",
                id,
                billId
        );

        var result =
                paymentService.updatePayment(
                        id,
                        billId,
                        payment
                );

        if (result.isEmpty()) {

            logger.warn(
                    "Payment or Bill not found. Payment ID: {}, Bill ID: {}",
                    id,
                    billId
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Payment or Bill not found"
                    );
        }

        logger.info(
                "Payment updated successfully. Payment ID: {}",
                id
        );

        return ResponseEntity.ok(
                result.get()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(
            @PathVariable Long id) {

        logger.info(
                "DELETE /api/payments/{} - Deleting payment",
                id
        );

        boolean deleted =
                paymentService.deletePayment(id);

        if (!deleted) {

            logger.warn(
                    "Cannot delete. Payment not found with ID: {}",
                    id
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Payment not found with id: "
                                    + id
                    );
        }

        logger.info(
                "Payment deleted successfully with ID: {}",
                id
        );

        return ResponseEntity.ok(
                "Payment deleted successfully"
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>>
    getPaymentSummary() {

        logger.info(
                "GET /api/payments/summary - Generating payment summary"
        );

        Map<String, Object> summary =
                paymentService.getPaymentSummary();

        logger.info(
                "Payment summary returned successfully"
        );

        return ResponseEntity.ok(summary);
    }
}