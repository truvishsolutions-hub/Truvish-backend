package com.truvish.truvishbackend.redemption;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RedemptionHistoryItemResponse {

    private Long userId;
    private Long clientId;
    private String clientCompanyName;
    private String userPhoneNumber;
    private String userTruvishCode;
    private String userBrandName;
    private Long userBrandValue;
    private String userBrandVoucher;
    private String userBrandPin;
    private LocalDate userBrandValidity;
    private LocalDateTime userBrandTimeTemp;
    private Long beforeBalance;
    private Long afterBalance;
    private String historyMessage;
    private String redeemStatus;
    private String brandLogo;
    private String redemptionProcess;

    public RedemptionHistoryItemResponse(
            Long userId,
            Long clientId,
            String clientCompanyName,
            String userPhoneNumber,
            String userTruvishCode,
            String userBrandName,
            Long userBrandValue,
            String userBrandVoucher,
            String userBrandPin,
            LocalDate userBrandValidity,
            LocalDateTime userBrandTimeTemp,
            Long beforeBalance,
            Long afterBalance,
            String historyMessage,
            String redeemStatus,
            String brandLogo,
            String redemptionProcess
    ) {
        this.userId = userId;
        this.clientId = clientId;
        this.clientCompanyName = clientCompanyName;
        this.userPhoneNumber = userPhoneNumber;
        this.userTruvishCode = userTruvishCode;
        this.userBrandName = userBrandName;
        this.userBrandValue = userBrandValue;
        this.userBrandVoucher = userBrandVoucher;
        this.userBrandPin = userBrandPin;
        this.userBrandValidity = userBrandValidity;
        this.userBrandTimeTemp = userBrandTimeTemp;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.historyMessage = historyMessage;
        this.redeemStatus = redeemStatus;
        this.brandLogo = brandLogo;
        this.redemptionProcess = redemptionProcess;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public String getUserTruvishCode() {
        return userTruvishCode;
    }

    public String getUserBrandName() {
        return userBrandName;
    }

    public Long getUserBrandValue() {
        return userBrandValue;
    }

    public String getUserBrandVoucher() {
        return userBrandVoucher;
    }

    public String getUserBrandPin() {
        return userBrandPin;
    }

    public LocalDate getUserBrandValidity() {
        return userBrandValidity;
    }

    public LocalDateTime getUserBrandTimeTemp() {
        return userBrandTimeTemp;
    }

    public Long getBeforeBalance() {
        return beforeBalance;
    }

    public Long getAfterBalance() {
        return afterBalance;
    }

    public String getHistoryMessage() {
        return historyMessage;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }
}