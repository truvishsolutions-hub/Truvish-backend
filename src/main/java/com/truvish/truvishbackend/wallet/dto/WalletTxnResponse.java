package com.truvish.truvishbackend.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WalletTxnResponse {

    private Long txnId;
    private LocalDateTime txnDateTime;
    private BigDecimal amount;
    private String type;
    private String description;
    private String referenceType;
    private String referenceId;
    private String status;
    private LocalDateTime createdAt;

    public WalletTxnResponse(
            Long txnId,
            LocalDateTime txnDateTime,
            BigDecimal amount,
            String type,
            String description,
            String referenceType,
            String referenceId,
            String status,
            LocalDateTime createdAt
    ) {
        this.txnId = txnId;
        this.txnDateTime = txnDateTime;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getTxnId() {
        return txnId;
    }

    public LocalDateTime getTxnDateTime() {
        return txnDateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}