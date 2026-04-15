package com.truvish.truvishbackend.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class CreateWalletTxnRequest {

    // ✅ Amount required & positive only
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    // ✅ Only CREDIT or DEBIT allowed
    @NotBlank(message = "Type is required (CREDIT or DEBIT)")
    private String type;

    // Optional
    private String description;
    private String referenceType;
    private String referenceId;

    // ======================
    // GETTERS & SETTERS
    // ======================

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type == null ? null : type.trim().toUpperCase();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description == null ? null : description.trim();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceType() {
        return referenceType == null ? null : referenceType.trim().toUpperCase();
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceId() {
        return referenceId == null ? null : referenceId.trim();
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}