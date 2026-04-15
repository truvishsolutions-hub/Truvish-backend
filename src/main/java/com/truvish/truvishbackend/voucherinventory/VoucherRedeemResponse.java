package com.truvish.truvishbackend.voucherinventory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class VoucherRedeemResponse {

    private String brandName;
    private BigDecimal denomination;
    private String voucher;
    private String pin;
    private LocalDate validityTill;
    private LocalDateTime redeemedAt;

    public VoucherRedeemResponse(
            String brandName,
            BigDecimal denomination,
            String voucher,
            String pin,
            LocalDate validityTill,
            LocalDateTime redeemedAt
    ) {
        this.brandName = brandName;
        this.denomination = denomination;
        this.voucher = voucher;
        this.pin = pin;
        this.validityTill = validityTill;
        this.redeemedAt = redeemedAt;
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

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }
}
