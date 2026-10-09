package com.paypilot.paypilot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypilot.paypilot.entity.Bill;
import com.paypilot.paypilot.service.BillService;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    private static final Logger logger =
            LoggerFactory.getLogger(BillController.class);

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;

        logger.info("BillController initialized successfully");
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(
            @RequestBody Bill bill) {

        logger.info(
                "POST /api/bills - Creating bill: {}",
                bill.getBillName()
        );

        Bill savedBill =
                billService.createBill(bill);

        logger.info(
                "POST /api/bills - Bill created with ID: {}",
                savedBill.getId()
        );

        return new ResponseEntity<>(
                savedBill,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {

        logger.info(
                "GET /api/bills - Fetching all bills"
        );

        return ResponseEntity.ok(
                billService.getAllBills()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(
            @PathVariable Long id) {

        logger.info(
                "GET /api/bills/{} - Fetching bill",
                id
        );

        return billService.getBillById(id)
                .map(bill -> {

                    logger.info(
                            "Bill found with ID: {}",
                            id
                    );

                    return ResponseEntity.ok(bill);
                })
                .orElseGet(() -> {

                    logger.warn(
                            "Bill not found with ID: {}",
                            id
                    );

                    return ResponseEntity.notFound().build();
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(
            @PathVariable Long id,
            @RequestBody Bill bill) {

        logger.info(
                "PUT /api/bills/{} - Updating bill",
                id
        );

        return billService.updateBill(id, bill)
                .map(updatedBill -> {

                    logger.info(
                            "Bill updated successfully with ID: {}",
                            id
                    );

                    return ResponseEntity.ok(updatedBill);
                })
                .orElseGet(() -> {

                    logger.warn(
                            "Cannot update. Bill not found with ID: {}",
                            id
                    );

                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(
            @PathVariable Long id) {

        logger.info(
                "DELETE /api/bills/{} - Deleting bill",
                id
        );

        boolean deleted =
                billService.deleteBill(id);

        if (!deleted) {

            logger.warn(
                    "Cannot delete. Bill not found with ID: {}",
                    id
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Bill not found with id: " + id
                    );
        }

        logger.info(
                "Bill deleted successfully with ID: {}",
                id
        );

        return ResponseEntity.ok(
                "Bill deleted successfully"
        );
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Bill>> getUpcomingBills() {

        logger.info(
                "GET /api/bills/upcoming - Fetching upcoming bills"
        );

        return ResponseEntity.ok(
                billService.getUpcomingBills()
        );
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Bill>> getOverdueBills() {

        logger.info(
                "GET /api/bills/overdue - Fetching overdue bills"
        );

        return ResponseEntity.ok(
                billService.getOverdueBills()
        );
    }

    @GetMapping("/paid")
    public ResponseEntity<List<Bill>> getPaidBills() {

        logger.info(
                "GET /api/bills/paid - Fetching paid bills"
        );

        return ResponseEntity.ok(
                billService.getPaidBills()
        );
    }

    @GetMapping("/cancelled")
    public ResponseEntity<List<Bill>> getCancelledBills() {

        logger.info(
                "GET /api/bills/cancelled - Fetching cancelled bills"
        );

        return ResponseEntity.ok(
                billService.getCancelledBills()
        );
    }
}