package com.truvish.truvishbackend.redemption;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CodeRedemptionHistoryResponse {

    private Long id;
    private Long clientId;
    private String clientCompanyName;
    private String truvishCode;
    private String phoneNumber;
    private String brandName;
    private Long redeemedValue;
    private String voucherCode;
    private String voucherPin;
    private LocalDate validityTill;
    private Long beforeBalance;
    private Long afterBalance;
    private String redeemStatus;
    private String historyMessage;
    private String brandLogo;
    private String redemptionProcess;
    private LocalDateTime redeemedAt;
    private LocalDateTime createdAt;

    public CodeRedemptionHistoryResponse(
            Long id,
            Long clientId,
            String clientCompanyName,
            String truvishCode,
            String phoneNumber,
            String brandName,
            Long redeemedValue,
            String voucherCode,
            String voucherPin,
            LocalDate validityTill,
            Long beforeBalance,
            Long afterBalance,
            String redeemStatus,
            String historyMessage,
            String brandLogo,
            String redemptionProcess,
            LocalDateTime redeemedAt,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.clientId = clientId;
        this.clientCompanyName = clientCompanyName;
        this.truvishCode = truvishCode;
        this.phoneNumber = phoneNumber;
        this.brandName = brandName;
        this.redeemedValue = redeemedValue;
        this.voucherCode = voucherCode;
        this.voucherPin = voucherPin;
        this.validityTill = validityTill;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.redeemStatus = redeemStatus;
        this.historyMessage = historyMessage;
        this.brandLogo = brandLogo;
        this.redemptionProcess = redemptionProcess;
        this.redeemedAt = redeemedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public String getTruvishCode() {
        return truvishCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public Long getRedeemedValue() {
        return redeemedValue;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getVoucherPin() {
        return voucherPin;
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

    public String getHistoryMessage() {
        return historyMessage;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}