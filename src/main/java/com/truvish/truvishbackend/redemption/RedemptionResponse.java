package com.truvish.truvishbackend.redemption;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RedemptionResponse {

    private String brandName;
    private Long redeemedValue;
    private String voucher;
    private String pin;
    private LocalDate validityTill;
    private Long beforeBalance;
    private Long afterBalance;
    private String redeemStatus;
    private String message;
    private LocalDateTime redeemedAt;

    public RedemptionResponse(
            String brandName,
            Long redeemedValue,
            String voucher,
            String pin,
            LocalDate validityTill,
            Long beforeBalance,
            Long afterBalance,
            String redeemStatus,
            String message,
            LocalDateTime redeemedAt
    ) {
        this.brandName = brandName;
        this.redeemedValue = redeemedValue;
        this.voucher = voucher;
        this.pin = pin;
        this.validityTill = validityTill;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.redeemStatus = redeemStatus;
        this.message = message;
        this.redeemedAt = redeemedAt;
    }

    public String getBrandName() {
        return brandName;
    }

    public Long getRedeemedValue() {
        return redeemedValue;
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

    public Long getBeforeBalance() {
        return beforeBalance;
    }

    public Long getAfterBalance() {
        return afterBalance;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }
}