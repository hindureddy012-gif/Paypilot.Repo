
package com.paypilot.service;

import com.paypilot.dto.BillRequest;
import com.paypilot.entity.Bill;
import com.paypilot.entity.BillCategory;
import com.paypilot.entity.BillStatus;
import com.paypilot.exception.BillNotFoundException;
import com.paypilot.repository.BillRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillServiceImpl billService;

    private Bill bill;
    private BillRequest billRequest;

    @BeforeEach
    void setUp() {

        bill = new Bill();

        bill.setId(1L);
        bill.setUserId(100L);
        bill.setBillName("Electricity Bill");
        bill.setBillingCompany("ABC Electricity");
        bill.setAmount(new BigDecimal("1500.00"));
        bill.setDueDate(LocalDate.now().plusDays(10));
        bill.setCategory(BillCategory.ELECTRICITY);
        bill.setStatus(BillStatus.UPCOMING);
        bill.setRecurring(false);
        bill.setRecurrenceType(null);
        bill.setReminderEnabled(true);
        bill.setReminderDaysBefore(3);

        billRequest = new BillRequest();

        billRequest.setUserId(100L);
        billRequest.setBillName("Electricity Bill");
        billRequest.setBillingCompany("ABC Electricity");
        billRequest.setAmount(new BigDecimal("1500.00"));
        billRequest.setDueDate(LocalDate.now().plusDays(10));
        billRequest.setCategory(BillCategory.ELECTRICITY);
        billRequest.setRecurring(false);
        billRequest.setRecurrenceType(null);
        billRequest.setReminderEnabled(true);
        billRequest.setReminderDaysBefore(3);
    }


    // ---------------------------------------------------------
    // ADD BILL
    // ---------------------------------------------------------

    @Test
    void shouldAddBill() {

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill);

        Bill result = billService.addBill(billRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electricity Bill", result.getBillName());
        assertEquals(BillStatus.UPCOMING, result.getStatus());

        verify(billRepository, times(1))
                .save(any(Bill.class));
    }


    // ---------------------------------------------------------
    // GET ALL BILLS
    // ---------------------------------------------------------

    @Test
    void shouldGetAllBills() {

        Bill secondBill = new Bill();
        secondBill.setId(2L);
        secondBill.setBillName("Internet Bill");

        List<Bill> bills = Arrays.asList(
                bill,
                secondBill
        );

        when(billRepository.findAll())
                .thenReturn(bills);

        List<Bill> result = billService.getAllBills();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electricity Bill",
                result.get(0).getBillName());

        verify(billRepository, times(1))
                .findAll();
    }


    // ---------------------------------------------------------
    // GET BILLS BY USER
    // ---------------------------------------------------------

    @Test
    void shouldGetBillsByUser() {

        Long userId = 100L;

        when(billRepository.findByUserId(userId))
                .thenReturn(Collections.singletonList(bill));

        List<Bill> result =
                billService.getBillsByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getUserId());

        verify(billRepository, times(1))
                .findByUserId(userId);
    }


    // ---------------------------------------------------------
    // GET BILL BY ID
    // ---------------------------------------------------------

    @Test
    void shouldGetBillById() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        Bill result =
                billService.getBillById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Electricity Bill",
                result.getBillName());

        verify(billRepository, times(1))
                .findById(1L);
    }


    // ---------------------------------------------------------
    // GET BILL BY ID - NOT FOUND
    // ---------------------------------------------------------

    @Test
    void shouldThrowExceptionWhenBillNotFound() {

        when(billRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                BillNotFoundException.class,
                () -> billService.getBillById(999L)
        );

        verify(billRepository, times(1))
                .findById(999L);
    }


    // ---------------------------------------------------------
    // UPDATE BILL
    // ---------------------------------------------------------

    @Test
    void shouldUpdateBill() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill);

        BillRequest updateRequest = new BillRequest();

        updateRequest.setUserId(100L);
        updateRequest.setBillName("Updated Electricity Bill");
        updateRequest.setBillingCompany("Updated Company");
        updateRequest.setAmount(new BigDecimal("2000.00"));
        updateRequest.setDueDate(LocalDate.now().plusDays(20));
        updateRequest.setCategory(BillCategory.ELECTRICITY);
        updateRequest.setRecurring(true);
        updateRequest.setRecurrenceType("MONTHLY");
        updateRequest.setReminderEnabled(true);
        updateRequest.setReminderDaysBefore(5);

        Bill result =
                billService.updateBill(1L, updateRequest);

        assertNotNull(result);
        assertEquals(
                "Updated Electricity Bill",
                result.getBillName()
        );

        assertEquals(
                new BigDecimal("2000.00"),
                result.getAmount()
        );

        assertTrue(result.getRecurring());
        assertEquals("MONTHLY",
                result.getRecurrenceType());

        verify(billRepository, times(1))
                .findById(1L);

        verify(billRepository, times(1))
                .save(bill);
    }


    // ---------------------------------------------------------
    // DELETE BILL
    // ---------------------------------------------------------

    @Test
    void shouldDeleteBill() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        billService.deleteBill(1L);

        verify(billRepository, times(1))
                .findById(1L);

        verify(billRepository, times(1))
                .delete(bill);
    }


    // ---------------------------------------------------------
    // GET UPCOMING BILLS
    // ---------------------------------------------------------

    @Test
    void shouldGetUpcomingBills() {

        Long userId = 100L;

        when(
                billRepository.findByUserIdAndDueDateBetween(
                        eq(userId),
                        any(LocalDate.class),
                        any(LocalDate.class)
                )
        ).thenReturn(Collections.singletonList(bill));

        List<Bill> result =
                billService.getUpcomingBills(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(billRepository, times(1))
                .findByUserIdAndDueDateBetween(
                        eq(userId),
                        any(LocalDate.class),
                        any(LocalDate.class)
                );
    }


    // ---------------------------------------------------------
    // GET OVERDUE BILLS
    // ---------------------------------------------------------

    @Test
    void shouldGetOverdueBills() {

        Long userId = 100L;

        bill.setStatus(BillStatus.OVERDUE);

        when(
                billRepository.findByUserIdAndStatus(
                        userId,
                        BillStatus.OVERDUE
                )
        ).thenReturn(Collections.singletonList(bill));

        List<Bill> result =
                billService.getOverdueBills(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                BillStatus.OVERDUE,
                result.get(0).getStatus()
        );

        verify(billRepository, times(1))
                .findByUserIdAndStatus(
                        userId,
                        BillStatus.OVERDUE
                );
    }


    // ---------------------------------------------------------
    // MARK AS PAID
    // ---------------------------------------------------------

    @Test
    void shouldMarkBillAsPaid() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill);

        Bill result =
                billService.markAsPaid(1L);

        assertNotNull(result);
        assertEquals(
                BillStatus.PAID,
                result.getStatus()
        );

        verify(billRepository, times(1))
                .findById(1L);

        verify(billRepository, times(1))
                .save(bill);
    }


    // ---------------------------------------------------------
    // SNOOZE BILL
    // ---------------------------------------------------------

    @Test
    void shouldSnoozeBill() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill);

        int days = 5;

        Bill result =
                billService.snoozeBill(1L, days);

        assertNotNull(result);

        assertEquals(
                LocalDate.now().plusDays(5),
                result.getSnoozedUntil()
        );

        verify(billRepository, times(1))
                .findById(1L);

        verify(billRepository, times(1))
                .save(bill);
    }


    // ---------------------------------------------------------
    // UPDATE REMINDER SETTINGS
    // ---------------------------------------------------------

    @Test
    void shouldUpdateReminderSettings() {

        when(billRepository.findById(1L))
                .thenReturn(Optional.of(bill));

        when(billRepository.save(any(Bill.class)))
                .thenReturn(bill);

        Bill result =
                billService.updateReminderSettings(
                        1L,
                        false,
                        7
                );

        assertNotNull(result);

        assertFalse(result.getReminderEnabled());

        assertEquals(
                7,
                result.getReminderDaysBefore()
        );

        verify(billRepository, times(1))
                .findById(1L);

        verify(billRepository, times(1))
                .save(bill);
    }


    // ---------------------------------------------------------
    // UPDATE OVERDUE BILLS
    // ---------------------------------------------------------

    @Test
    void shouldMarkPastDueBillsAsOverdue() {

        Bill overdueBill = new Bill();

        overdueBill.setId(2L);
        overdueBill.setDueDate(
                LocalDate.now().minusDays(2)
        );
        overdueBill.setStatus(
                BillStatus.UPCOMING
        );

        Bill paidBill = new Bill();

        paidBill.setId(3L);
        paidBill.setDueDate(
                LocalDate.now().minusDays(2)
        );
        paidBill.setStatus(
                BillStatus.PAID
        );

        when(billRepository.findAll())
                .thenReturn(
                        Arrays.asList(
                                overdueBill,
                                paidBill
                        )
                );

        billService.updateOverdueBills();

        assertEquals(
                BillStatus.OVERDUE,
                overdueBill.getStatus()
        );

        assertEquals(
                BillStatus.PAID,
                paidBill.getStatus()
        );

        verify(billRepository, times(1))
                .findAll();

        verify(billRepository, times(1))
                .save(overdueBill);

        verify(billRepository, never())
                .save(paidBill);
    }
}

