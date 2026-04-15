package com.truvish.truvishbackend.voucherinventory;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "voucher_inventory",
        indexes = {
                @Index(name = "idx_voucher_inventory_brand_denom", columnList = "brand_name, denomination"),
                @Index(name = "idx_voucher_inventory_validity", columnList = "validity_till"),
                @Index(name = "idx_voucher_inventory_status", columnList = "status")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_voucher_inventory_code", columnNames = "voucher_code")
        }
)
public class VoucherInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name", nullable = false, length = 150)
    private String brandName;

    @Column(name = "denomination", nullable = false, precision = 12, scale = 2)
    private BigDecimal denomination;

    @Column(name = "voucher_code", nullable = false, length = 255)
    private String voucherCode;

    @Column(name = "voucher_pin", nullable = false, length = 255)
    private String voucherPin;

    @Column(name = "validity_till", nullable = false)
    private LocalDate validityTill;

    @Column(name = "redemption_process", columnDefinition = "text")
    private String redemptionProcess;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "used_by", length = 200)
    private String usedBy;

    @Column(name = "used_order_reference", length = 100)
    private String usedOrderReference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherPin() {
        return voucherPin;
    }

    public void setVoucherPin(String voucherPin) {
        this.voucherPin = voucherPin;
    }

    public LocalDate getValidityTill() {
        return validityTill;
    }

    public void setValidityTill(LocalDate validityTill) {
        this.validityTill = validityTill;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }

    public void setRedemptionProcess(String redemptionProcess) {
        this.redemptionProcess = redemptionProcess;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public String getUsedBy() {
        return usedBy;
    }

    public void setUsedBy(String usedBy) {
        this.usedBy = usedBy;
    }

    public String getUsedOrderReference() {
        return usedOrderReference;
    }

    public void setUsedOrderReference(String usedOrderReference) {
        this.usedOrderReference = usedOrderReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}