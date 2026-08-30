package com.truvish.truvishbackend.TruCard.entity;

import com.truvish.truvishbackend.TruCard.enums.TruCardOrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trucard_orders")
public class TruCardOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Client who ordered the TruCards
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    // Campaign selected for this order
    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    // Denomination of each card
    @Column(name = "denomination", nullable = false, precision = 12, scale = 2)
    private BigDecimal denomination;

    // Number of cards ordered
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // denomination × quantity
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    // Client wallet balance before order
    @Column(name = "balance_before", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceBefore;

    // Client wallet balance after order
    @Column(name = "balance_after", nullable = false, precision = 12, scale = 2)
    private BigDecimal balanceAfter;

    // Order status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TruCardOrderStatus status;

    // When order was created
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // When order was last updated
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =========================
    // Constructors
    // =========================

    public TruCardOrder() {
    }


    // =========================
    // Pre Persist
    // =========================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = TruCardOrderStatus.COMPLETED;
        }
    }


    // =========================
    // Pre Update
    // =========================

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // =========================
    // Getters & Setters
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }


    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }


    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }


    public TruCardOrderStatus getStatus() {
        return status;
    }

    public void setStatus(TruCardOrderStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}