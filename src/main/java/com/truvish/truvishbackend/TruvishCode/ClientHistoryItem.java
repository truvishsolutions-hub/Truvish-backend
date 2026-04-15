package com.truvish.truvishbackend.TruvishCode;

import java.time.LocalDateTime;

public class ClientHistoryItem {

    private LocalDateTime eventTime;
    private String code;
    private Long amount;
    private Long remainingBalance;
    private String message;
    private String eventType;
    private Integer validityMonths;
    private LocalDateTime expiryDate;

    public ClientHistoryItem() {
    }

    public ClientHistoryItem(
            LocalDateTime eventTime,
            String code,
            Long amount,
            Long remainingBalance,
            String message,
            String eventType,
            Integer validityMonths,
            LocalDateTime expiryDate
    ) {
        this.eventTime = eventTime;
        this.code = code;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
        this.message = message;
        this.eventType = eventType;
        this.validityMonths = validityMonths;
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(Long remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(Integer validityMonths) {
        this.validityMonths = validityMonths;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}