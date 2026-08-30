package com.truvish.truvishbackend.redemption;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RedemptionHistoryItemResponse {

    // =========================================================
    // COMMON
    // =========================================================

    private Long userId;

    private Long clientId;

    private String clientCompanyName;

    private String userPhoneNumber;

    private String userTruvishCode;

    // =========================================================
    // BRAND
    // =========================================================

    private String userBrandName;

    private Long userBrandValue;

    private String userBrandVoucher;

    private String userBrandPin;

    private LocalDate userBrandValidity;

    // =========================================================
    // DATE / TIME
    // =========================================================

    private LocalDateTime userBrandTimeTemp;

    // =========================================================
    // BALANCE
    // =========================================================

    private Long beforeBalance;

    private Long afterBalance;

    // =========================================================
    // HISTORY
    // =========================================================

    private String historyMessage;

    private String redeemStatus;

    private String brandLogo;

    private String redemptionProcess;

    // =========================================================
    // CODE TYPE
    //
    // DIGITAL / PHYSICAL
    // =========================================================

    private String codeType;

    // =========================================================
    // PHYSICAL TRUCARD
    // =========================================================

    private Long truBlankCodeId;

    private String serialNumber;

    private String referenceNumber;

    private String codeNumber;

    private Long denomination;

    private Integer validityMonths;

    private LocalDateTime expiryDate;

    private String blankCodeStatus;

    // =========================================================
    // CLIENT
    // =========================================================

    private String clientName;

    private String clientImg;

    private String clientCategory;

    private String clientTheme;

    private String clientThemeImg;

    // =========================================================
    // LIFECYCLE
    // =========================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime activatedAt;

    private Long activatedBy;

    private LocalDateTime redeemedAt;

    private Long createdBy;

    // =========================================================
    // CLIENT BALANCE SNAPSHOT
    // =========================================================

    private Long clientBalanceBeforeActivation;

    private Long clientBalanceAfterActivation;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RedemptionHistoryItemResponse(

            // COMMON
            Long userId,
            Long clientId,
            String clientCompanyName,
            String userPhoneNumber,
            String userTruvishCode,

            // BRAND
            String userBrandName,
            Long userBrandValue,
            String userBrandVoucher,
            String userBrandPin,
            LocalDate userBrandValidity,

            // DATE
            LocalDateTime userBrandTimeTemp,

            // BALANCE
            Long beforeBalance,
            Long afterBalance,

            // HISTORY
            String historyMessage,
            String redeemStatus,
            String brandLogo,
            String redemptionProcess,

            // CODE TYPE
            String codeType,

            // PHYSICAL
            Long truBlankCodeId,
            String serialNumber,
            String referenceNumber,
            String codeNumber,
            Long denomination,
            Integer validityMonths,
            LocalDateTime expiryDate,
            String blankCodeStatus,

            // CLIENT
            String clientName,
            String clientImg,
            String clientCategory,
            String clientTheme,
            String clientThemeImg,

            // LIFECYCLE
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime activatedAt,
            Long activatedBy,
            LocalDateTime redeemedAt,
            Long createdBy,

            // BALANCE SNAPSHOT
            Long clientBalanceBeforeActivation,
            Long clientBalanceAfterActivation
    ) {

        this.userId = userId;

        this.clientId = clientId;

        this.clientCompanyName =
                clientCompanyName;

        this.userPhoneNumber =
                userPhoneNumber;

        this.userTruvishCode =
                userTruvishCode;

        // =====================================================
        // BRAND
        // =====================================================

        this.userBrandName =
                userBrandName;

        this.userBrandValue =
                userBrandValue;

        this.userBrandVoucher =
                userBrandVoucher;

        this.userBrandPin =
                userBrandPin;

        this.userBrandValidity =
                userBrandValidity;

        // =====================================================
        // DATE
        // =====================================================

        this.userBrandTimeTemp =
                userBrandTimeTemp;

        // =====================================================
        // BALANCE
        // =====================================================

        this.beforeBalance =
                beforeBalance;

        this.afterBalance =
                afterBalance;

        // =====================================================
        // HISTORY
        // =====================================================

        this.historyMessage =
                historyMessage;

        this.redeemStatus =
                redeemStatus;

        this.brandLogo =
                brandLogo;

        this.redemptionProcess =
                redemptionProcess;

        // =====================================================
        // CODE TYPE
        // =====================================================

        this.codeType =
                codeType;

        // =====================================================
        // PHYSICAL
        // =====================================================

        this.truBlankCodeId =
                truBlankCodeId;

        this.serialNumber =
                serialNumber;

        this.referenceNumber =
                referenceNumber;

        this.codeNumber =
                codeNumber;

        this.denomination =
                denomination;

        this.validityMonths =
                validityMonths;

        this.expiryDate =
                expiryDate;

        this.blankCodeStatus =
                blankCodeStatus;

        // =====================================================
        // CLIENT
        // =====================================================

        this.clientName =
                clientName;

        this.clientImg =
                clientImg;

        this.clientCategory =
                clientCategory;

        this.clientTheme =
                clientTheme;

        this.clientThemeImg =
                clientThemeImg;

        // =====================================================
        // LIFECYCLE
        // =====================================================

        this.createdAt =
                createdAt;

        this.updatedAt =
                updatedAt;

        this.activatedAt =
                activatedAt;

        this.activatedBy =
                activatedBy;

        this.redeemedAt =
                redeemedAt;

        this.createdBy =
                createdBy;

        // =====================================================
        // BALANCE SNAPSHOT
        // =====================================================

        this.clientBalanceBeforeActivation =
                clientBalanceBeforeActivation;

        this.clientBalanceAfterActivation =
                clientBalanceAfterActivation;
    }

    // =========================================================
    // GETTERS
    // =========================================================

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

    public String getCodeType() {
        return codeType;
    }

    public Long getTruBlankCodeId() {
        return truBlankCodeId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public Long getDenomination() {
        return denomination;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public String getBlankCodeStatus() {
        return blankCodeStatus;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientImg() {
        return clientImg;
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public String getClientTheme() {
        return clientTheme;
    }

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public Long getActivatedBy() {
        return activatedBy;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Long getClientBalanceBeforeActivation() {
        return clientBalanceBeforeActivation;
    }

    public Long getClientBalanceAfterActivation() {
        return clientBalanceAfterActivation;
    }
}