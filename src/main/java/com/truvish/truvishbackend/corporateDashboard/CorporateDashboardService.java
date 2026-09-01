package com.truvish.truvishbackend.corporateDashboard;

import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeRepository;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeStatus;
import com.truvish.truvishbackend.TruOpeAdmin.TruvishCodeRepository;
import com.truvish.truvishbackend.TruOpeAdmin.VoucherStatus;
import com.truvish.truvishbackend.client.Client;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CorporateDashboardService {

    private final CorporateDashboardRepository corporateDashboardRepository;

    private final TruvishCodeRepository truvishCodeRepository;

    private final TruBlankCodeRepository truBlankCodeRepository;


    // =========================================================
    // GET CORPORATE DASHBOARD
    // =========================================================

    @Transactional(readOnly = true)
    public CorporateDashboardResponse getDashboard(
            Long clientId
    ) {

        // =====================================================
        // VALIDATE CLIENT
        // =====================================================

        if (
                clientId == null
                        || clientId < 1
        ) {

            throw new RuntimeException(
                    "Valid clientId is required"
            );
        }


        // =====================================================
        // FIND CLIENT
        // =====================================================

        Client client =
                corporateDashboardRepository
                        .findById(clientId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Client not found with ID: "
                                                + clientId
                                )
                        );


        // =====================================================
        // CLIENT LOGO
        // =====================================================

        String logoUrl = null;

        if (
                client.getLogoImg() != null
                        && !client.getLogoImg().isBlank()
        ) {

            String logo =
                    client.getLogoImg().trim();

            if (logo.startsWith("/")) {

                logoUrl =
                        "http://localhost:8080"
                                + logo;

            } else {

                logoUrl =
                        "http://localhost:8080/uploads/"
                                + logo;
            }
        }


        // =====================================================
        // WALLET
        // =====================================================

        BigDecimal walletBalance =
                client.getBalance() != null
                        ? client.getBalance()
                        : BigDecimal.ZERO;

        walletBalance =
                walletBalance.setScale(
                        2,
                        RoundingMode.HALF_UP
                );


        // =========================================================
        // DIGITAL TRUVISH CODE
        // =========================================================


        // =====================================================
        // DIGITAL ISSUED COUNT
        // =====================================================

        Long digitalIssuedCodes =
                safeLong(
                        truvishCodeRepository
                                .countByClientId(
                                        clientId
                                )
                );


        // =====================================================
        // DIGITAL ISSUED VALUE
        // =====================================================

        BigDecimal digitalIssuedValue =
                safeDecimal(
                        truvishCodeRepository
                                .sumTruvishCodeValueByClientId(
                                        clientId
                                )
                );


        // =====================================================
        // DIGITAL REDEEMED COUNT
        // =====================================================

        Long digitalRedeemedCodes =
                safeLong(
                        truvishCodeRepository
                                .countByClientIdAndTruvishCodeStatus(
                                        clientId,
                                        VoucherStatus.REDEEMED
                                )
                );


        // =====================================================
        // DIGITAL REDEEMED VALUE
        // =====================================================

        BigDecimal digitalRedeemedValue =
                safeDecimal(
                        truvishCodeRepository
                                .sumOriginalCodeValueByClientIdAndStatus(
                                        clientId,
                                        VoucherStatus.REDEEMED
                                )
                );


        // =====================================================
        // DIGITAL ACTIVE COUNT
        // =====================================================

        Long digitalActiveCodes =
                safeLong(
                        truvishCodeRepository
                                .countByClientIdAndTruvishCodeStatus(
                                        clientId,
                                        VoucherStatus.ACTIVE
                                )
                );


        // =====================================================
        // DIGITAL ACTIVE VALUE
        // =====================================================

        BigDecimal digitalActiveValue =
                safeDecimal(
                        truvishCodeRepository
                                .sumTruvishCodeValueByClientIdAndStatus(
                                        clientId,
                                        VoucherStatus.ACTIVE
                                )
                );


        // =====================================================
        // DIGITAL INACTIVE COUNT
        // =====================================================

        Long digitalInactiveCodes =
                safeLong(
                        truvishCodeRepository
                                .countByClientIdAndTruvishCodeStatus(
                                        clientId,
                                        VoucherStatus.INACTIVE
                                )
                );


        // =====================================================
        // DIGITAL INACTIVE VALUE
        // =====================================================

        BigDecimal digitalInactiveValue =
                safeDecimal(
                        truvishCodeRepository
                                .sumTruvishCodeValueByClientIdAndStatus(
                                        clientId,
                                        VoucherStatus.INACTIVE
                                )
                );


        // =====================================================
        // DIGITAL UNUSED
        //
        // UNUSED = ACTIVE + INACTIVE
        // =====================================================

        Long digitalUnusedCodes =
                digitalActiveCodes
                        + digitalInactiveCodes;


        BigDecimal digitalUnusedValue =
                digitalActiveValue
                        .add(digitalInactiveValue)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        // =====================================================
        // DIGITAL EXPIRED
        // =====================================================

        Long digitalExpiredCodes =
                safeLong(
                        truvishCodeRepository
                                .countByClientIdAndTruvishCodeStatus(
                                        clientId,
                                        VoucherStatus.EXPIRED
                                )
                );


        // =====================================================
        // DIGITAL REDEMPTION RATE
        // =====================================================

        BigDecimal digitalRedemptionRate =
                calculateRate(
                        digitalRedeemedCodes,
                        digitalIssuedCodes
                );


        // =====================================================
        // DIGITAL TOTAL USERS
        // =====================================================

        Long digitalTotalUsers =
                0L;


        // =========================================================
        // PHYSICAL TRUCARD
        // =========================================================


        // =====================================================
        // PHYSICAL ISSUED COUNT
        // =====================================================

        Long truIssuedCodes =
                safeLong(
                        truBlankCodeRepository
                                .countByClientId(
                                        clientId
                                )
                );


        // =====================================================
        // PHYSICAL ISSUED VALUE
        //
        // safeDecimal supports both:
        // BigDecimal
        // Long
        // =====================================================

        BigDecimal truIssuedValue =
                safeDecimal(
                        truBlankCodeRepository
                                .sumDenominationByClientId(
                                        clientId
                                )
                );


        // =====================================================
        // PHYSICAL REDEEMED COUNT
        // =====================================================

        Long truRedeemedCodes =
                safeLong(
                        truBlankCodeRepository
                                .countByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.REDEEMED
                                )
                );


        // =====================================================
        // PHYSICAL REDEEMED VALUE
        // =====================================================

        BigDecimal truRedeemedValue =
                safeDecimal(
                        truBlankCodeRepository
                                .sumDenominationByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.REDEEMED
                                )
                );


        // =====================================================
        // PHYSICAL ACTIVE COUNT
        // =====================================================

        Long truActiveCodes =
                safeLong(
                        truBlankCodeRepository
                                .countByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.ACTIVE
                                )
                );


        // =====================================================
        // PHYSICAL ACTIVE VALUE
        // =====================================================

        BigDecimal truActiveValue =
                safeDecimal(
                        truBlankCodeRepository
                                .sumDenominationByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.ACTIVE
                                )
                );


        // =====================================================
        // PHYSICAL INACTIVE COUNT
        // =====================================================

        Long truInactiveCodes =
                safeLong(
                        truBlankCodeRepository
                                .countByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.INACTIVE
                                )
                );


        // =====================================================
        // PHYSICAL INACTIVE VALUE
        // =====================================================

        BigDecimal truInactiveValue =
                safeDecimal(
                        truBlankCodeRepository
                                .sumDenominationByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.INACTIVE
                                )
                );


        // =====================================================
        // PHYSICAL UNUSED
        //
        // UNUSED = ACTIVE + INACTIVE
        // =====================================================

        Long truUnusedCodes =
                truActiveCodes
                        + truInactiveCodes;


        BigDecimal truUnusedValue =
                truActiveValue
                        .add(truInactiveValue)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );


        // =====================================================
        // PHYSICAL EXPIRED
        // =====================================================

        Long truExpiredCodes =
                safeLong(
                        truBlankCodeRepository
                                .countByClientIdAndStatus(
                                        clientId,
                                        TruBlankCodeStatus.EXPIRED
                                )
                );


        // =====================================================
        // PHYSICAL REDEMPTION RATE
        // =====================================================

        BigDecimal truRedemptionRate =
                calculateRate(
                        truRedeemedCodes,
                        truIssuedCodes
                );


        // =====================================================
        // PHYSICAL TOTAL USERS
        // =====================================================

        Long truTotalUsers =
                0L;


        // =====================================================
        // CASHBACK
        // =====================================================

        BigDecimal cashback =
                BigDecimal.ZERO.setScale(
                        2,
                        RoundingMode.HALF_UP
                );


        BigDecimal thisMonth =
                BigDecimal.ZERO.setScale(
                        2,
                        RoundingMode.HALF_UP
                );


        // =====================================================
        // BUILD RESPONSE
        // =====================================================

        return CorporateDashboardResponse
                .builder()


                // =================================================
                // CLIENT
                // =================================================

                .clientId(
                        client.getId()
                )

                .clientName(
                        client.getClientName()
                )

                .companyName(
                        client.getCompanyName()
                )

                .companyType(
                        "Enterprise Account"
                )

                .subTitle(
                        "Corporate Rewards Dashboard"
                )

                .email(
                        client.getEmail()
                )

                .mobileNumber(
                        client.getMobileNumber()
                )

                .logoImg(
                        logoUrl
                )


                // =================================================
                // WALLET
                // =================================================

                .walletBalance(
                        walletBalance
                )


                // =================================================
                // CASHBACK
                // =================================================

                .cashback(
                        cashback
                )

                .thisMonth(
                        thisMonth
                )


                // =================================================
                // DIGITAL
                // =================================================

                .digitalIssuedCodes(
                        digitalIssuedCodes
                )

                .digitalIssuedValue(
                        digitalIssuedValue
                )

                .digitalRedeemedCodes(
                        digitalRedeemedCodes
                )

                .digitalRedeemedValue(
                        digitalRedeemedValue
                )

                .digitalRedemptionRate(
                        digitalRedemptionRate
                )

                .digitalTotalUsers(
                        digitalTotalUsers
                )

                .digitalUnusedCodes(
                        digitalUnusedCodes
                )

                .digitalUnusedValue(
                        digitalUnusedValue
                )

                .digitalActiveCodes(
                        digitalActiveCodes
                )

                .digitalExpiredCodes(
                        digitalExpiredCodes
                )


                // =================================================
                // PHYSICAL TRUCARD
                // =================================================

                .truIssuedCodes(
                        truIssuedCodes
                )

                .truIssuedValue(
                        truIssuedValue
                )

                .truRedeemedCodes(
                        truRedeemedCodes
                )

                .truRedeemedValue(
                        truRedeemedValue
                )

                .truRedemptionRate(
                        truRedemptionRate
                )

                .truTotalUsers(
                        truTotalUsers
                )

                .truUnusedCodes(
                        truUnusedCodes
                )

                .truUnusedValue(
                        truUnusedValue
                )

                .truActiveCodes(
                        truActiveCodes
                )

                .truExpiredCodes(
                        truExpiredCodes
                )

                .build();
    }


    // =========================================================
    // CALCULATE REDEMPTION RATE
    // =========================================================

    private BigDecimal calculateRate(
            Long redeemed,
            Long issued
    ) {

        if (
                issued == null
                        || issued <= 0
        ) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }


        long redeemedValue =
                redeemed == null
                        ? 0L
                        : redeemed;


        return BigDecimal
                .valueOf(redeemedValue)
                .multiply(
                        BigDecimal.valueOf(100)
                )
                .divide(
                        BigDecimal.valueOf(issued),
                        2,
                        RoundingMode.HALF_UP
                );
    }


    // =========================================================
    // NULL SAFE LONG
    // =========================================================

    private Long safeLong(
            Long value
    ) {

        return value == null
                ? 0L
                : value;
    }


    // =========================================================
    // NULL SAFE DECIMAL
    //
    // BIGDECIMAL VERSION
    // =========================================================

    private BigDecimal safeDecimal(
            BigDecimal value
    ) {

        if (value == null) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }


        return value.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }


    // =========================================================
    // NULL SAFE DECIMAL
    //
    // LONG VERSION
    //
    // This fixes:
    //
    // safeDecimal(Long)
    //
    // =========================================================

    private BigDecimal safeDecimal(
            Long value
    ) {

        if (value == null) {

            return BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
            );
        }


        return BigDecimal
                .valueOf(value)
                .setScale(
                        2,
                        RoundingMode.HALF_UP
                );
    }
}