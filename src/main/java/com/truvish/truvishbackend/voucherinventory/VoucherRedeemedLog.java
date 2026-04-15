package com.truvish.truvishbackend.voucherinventory;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_redeemed_log")
public class VoucherRedeemedLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

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

    @Column(name = "redeemed_at", nullable = false)
    private LocalDateTime redeemedAt;

    @Column(name = "redeemed_by", length = 200)
    private String redeemedBy;

    @Column(name = "order_reference", length = 100)
    private String orderReference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
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

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public String getRedeemedBy() {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy) {
        this.redeemedBy = redeemedBy;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }
}
