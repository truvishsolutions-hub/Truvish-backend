package com.truvish.truvishbackend.corporateDashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CorporateDashboardResponse {

    // =========================================================
    // CLIENT
    // =========================================================

    private Long clientId;

    private String clientName;

    private String companyName;

    private String companyType;

    private String subTitle;

    private String email;

    private String mobileNumber;

    private String logoImg;

    // =========================================================
    // WALLET
    // =========================================================

    private BigDecimal walletBalance;

    // =========================================================
    // CASHBACK
    // =========================================================

    private BigDecimal cashback;

    private BigDecimal thisMonth;

    // =========================================================
    // DIGITAL TRUVISH CODE
    // truvish_code_generator
    // =========================================================

    private Long digitalIssuedCodes;

    private BigDecimal digitalIssuedValue;

    private Long digitalRedeemedCodes;

    private BigDecimal digitalRedeemedValue;

    private BigDecimal digitalRedemptionRate;

    private Long digitalTotalUsers;

    private Long digitalUnusedCodes;

    private BigDecimal digitalUnusedValue;

    private Long digitalActiveCodes;

    private Long digitalExpiredCodes;

    // =========================================================
    // PHYSICAL TRUCARD
    // truvish_blank_code
    // =========================================================

    private Long truIssuedCodes;

    private BigDecimal truIssuedValue;

    private Long truRedeemedCodes;

    private BigDecimal truRedeemedValue;

    private BigDecimal truRedemptionRate;

    private Long truTotalUsers;

    private Long truUnusedCodes;

    private BigDecimal truUnusedValue;

    private Long truActiveCodes;

    private Long truExpiredCodes;
}