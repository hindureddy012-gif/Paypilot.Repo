package com.paypilot.controller;

import com.paypilot.dto.BillRequest;
import com.paypilot.entity.Bill;
import com.paypilot.service.BillService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private static final Logger logger =
            LoggerFactory.getLogger(BillController.class);

    private final BillService billService;

    public BillController(BillService billService) {

        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<Bill> addBill(
            @Valid @RequestBody BillRequest request) {

        logger.info(
                "POST request received to add bill"
        );

        Bill bill =
                billService.addBill(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bill);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {

        logger.info(
                "GET request received to fetch all bills"
        );

        return ResponseEntity.ok(
                billService.getAllBills()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bill>> getBillsByUser(
            @PathVariable Long userId) {

        logger.info(
                "GET request received for bills of user ID: {}",
                userId
        );

        return ResponseEntity.ok(
                billService.getBillsByUser(userId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(
            @PathVariable Long id) {

        logger.info(
                "GET request received for bill ID: {}",
                id
        );

        return ResponseEntity.ok(
                billService.getBillById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(
            @PathVariable Long id,
            @Valid @RequestBody BillRequest request) {

        logger.info(
                "PUT request received to update bill ID: {}",
                id
        );

        return ResponseEntity.ok(
                billService.updateBill(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(
            @PathVariable Long id) {

        logger.info(
                "DELETE request received for bill ID: {}",
                id
        );

        billService.deleteBill(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/upcoming")
    public ResponseEntity<List<Bill>> getUpcomingBills(
            @PathVariable Long userId) {

        logger.info(
                "GET request received for upcoming bills. User ID: {}",
                userId
        );

        return ResponseEntity.ok(
                billService.getUpcomingBills(userId)
        );
    }

    @GetMapping("/user/{userId}/overdue")
    public ResponseEntity<List<Bill>> getOverdueBills(
            @PathVariable Long userId) {

        logger.info(
                "GET request received for overdue bills. User ID: {}",
                userId
        );

        return ResponseEntity.ok(
                billService.getOverdueBills(userId)
        );
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Bill> markAsPaid(
            @PathVariable Long id) {

        logger.info(
                "PUT request received to mark bill ID {} as paid",
                id
        );

        return ResponseEntity.ok(
                billService.markAsPaid(id)
        );
    }

    @PutMapping("/{id}/snooze")
    public ResponseEntity<Bill> snoozeBill(
            @PathVariable Long id,
            @RequestParam int days) {

        logger.info(
                "PUT request received to snooze bill ID {} for {} days",
                id,
                days
        );

        return ResponseEntity.ok(
                billService.snoozeBill(id, days)
        );
    }

    @PutMapping("/{id}/reminder")
    public ResponseEntity<Bill> updateReminderSettings(
            @PathVariable Long id,
            @RequestParam Boolean enabled,
            @RequestParam Integer daysBefore) {

        logger.info(
                "PUT request received to update reminder settings for bill ID {}",
                id
        );

        return ResponseEntity.ok(
                billService.updateReminderSettings(
                        id,
                        enabled,
                        daysBefore
                )
        );
    }
}