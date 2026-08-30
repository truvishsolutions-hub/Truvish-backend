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
    private String redeemedBrand;
    private String redeemedPhone;
    private LocalDateTime issuedDate;
    private LocalDateTime redeemedDate;
    private String codeType; // DIGITAL or PHYSICAL

    // NEW FIELDS
    private String serialNumber;
    private String referenceNumber;

    // Constructors
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
            LocalDateTime expiryDate,
            String redeemedBrand,
            String redeemedPhone,
            LocalDateTime issuedDate,
            LocalDateTime redeemedDate
    ) {
        this.eventTime = eventTime;
        this.code = code;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
        this.message = message;
        this.eventType = eventType;
        this.validityMonths = validityMonths;
        this.expiryDate = expiryDate;
        this.redeemedBrand = redeemedBrand;
        this.redeemedPhone = redeemedPhone;
        this.issuedDate = issuedDate;
        this.redeemedDate = redeemedDate;
    }

    // Getters and Setters for all fields

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

    public String getRedeemedBrand() {
        return redeemedBrand;
    }

    public void setRedeemedBrand(String redeemedBrand) {
        this.redeemedBrand = redeemedBrand;
    }

    public String getRedeemedPhone() {
        return redeemedPhone;
    }

    public void setRedeemedPhone(String redeemedPhone) {
        this.redeemedPhone = redeemedPhone;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(LocalDateTime redeemedDate) {
        this.redeemedDate = redeemedDate;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
}