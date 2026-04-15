package com.truvish.truvishbackend.voucherinventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VoucherInventoryCounterResponse {

    private Long id;
    private String brandName;
    private BigDecimal denomination;
    private String voucher;
    private String pin;
    private LocalDate validityTill;
    private String status;
    private LocalDateTime usedAt;

    public VoucherInventoryCounterResponse(
            Long id,
            String brandName,
            BigDecimal denomination,
            String voucher,
            String pin,
            LocalDate validityTill,
            String status,
            LocalDateTime usedAt
    ) {
        this.id = id;
        this.brandName = brandName;
        this.denomination = denomination;
        this.voucher = voucher;
        this.pin = pin;
        this.validityTill = validityTill;
        this.status = status;
        this.usedAt = usedAt;
    }

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public String getVoucher() {
        return voucher;
    }

    public String getPin() {
        return pin;
    }

    public LocalDate getValidityTill() {
        return validityTill;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }
}