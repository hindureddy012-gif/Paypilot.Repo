package com.paypilot.dto;

import com.paypilot.entity.BillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BillRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Bill name is required")
    private String billName;

    @NotBlank(message = "Billing company is required")
    private String billingCompany;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotNull(message = "Category is required")
    private BillCategory category;

    private Boolean recurring;

    private String recurrenceType;

    private Boolean reminderEnabled;

    private Integer reminderDaysBefore;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getBillingCompany() {
        return billingCompany;
    }

    public void setBillingCompany(String billingCompany) {
        this.billingCompany = billingCompany;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BillCategory getCategory() {
        return category;
    }

    public void setCategory(BillCategory category) {
        this.category = category;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public Boolean getReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(Boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    public Integer getReminderDaysBefore() {
        return reminderDaysBefore;
    }

    public void setReminderDaysBefore(Integer reminderDaysBefore) {
        this.reminderDaysBefore = reminderDaysBefore;
    }
}