package com.truvish.truvishbackend.corporateDashboard.CodesReport;

import java.time.LocalDateTime;

public class CodeReportResponse {

    // =========================================================
    // CODE
    // =========================================================

    private String code;


    // =========================================================
    // DENOMINATION
    // =========================================================

    private Long denomination;


    // =========================================================
    // STATUS
    // Redeemed
    // Active
    // Expired-Back to wallet
    // =========================================================

    private String status;


    // =========================================================
    // ISSUED DATE & TIME
    // =========================================================

    private LocalDateTime issuedDateTime;


    // =========================================================
    // VALIDITY PERIOD
    // =========================================================

    private String validityPeriod;


    // =========================================================
    // EXPIRY DATE
    // =========================================================

    private LocalDateTime expiryDate;


    // =========================================================
    // CAMPAIGN NAME
    //
    // Database:
    // client_theme
    // =========================================================

    private String campaignName;


    // =========================================================
    // THEME IMAGE
    //
    // Database:
    // client_theme_img
    // =========================================================

    private String theme;


    // =========================================================
    // REDEEMED BY
    //
    // Database:
    // user_phone_number
    // =========================================================

    private String redeemedBy;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public CodeReportResponse(
            String code,
            Long denomination,
            String status,
            LocalDateTime issuedDateTime,
            String validityPeriod,
            LocalDateTime expiryDate,
            String campaignName,
            String theme,
            String redeemedBy
    ) {

        this.code = code;
        this.denomination = denomination;
        this.status = status;
        this.issuedDateTime = issuedDateTime;
        this.validityPeriod = validityPeriod;
        this.expiryDate = expiryDate;
        this.campaignName = campaignName;
        this.theme = theme;
        this.redeemedBy = redeemedBy;
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public String getCode() {
        return code;
    }

    public Long getDenomination() {
        return denomination;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getIssuedDateTime() {
        return issuedDateTime;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getTheme() {
        return theme;
    }

    public String getRedeemedBy() {
        return redeemedBy;
    }
}