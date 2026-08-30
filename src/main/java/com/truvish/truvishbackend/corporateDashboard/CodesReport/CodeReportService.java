package com.truvish.truvishbackend.corporateDashboard.CodesReport;

import com.truvish.truvishbackend.TruvishCode.TruvishCode;
import com.truvish.truvishbackend.TruvishCode.TruvishCodeRepository;
import com.truvish.truvishbackend.TruvishCode.VoucherStatus;
import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CodeReportService {

    private final TruvishCodeRepository truvishCodeRepository;
    private final UserRedemptionRepository userRedemptionRepository;

    public CodeReportService(
            TruvishCodeRepository truvishCodeRepository,
            UserRedemptionRepository userRedemptionRepository
    ) {
        this.truvishCodeRepository = truvishCodeRepository;
        this.userRedemptionRepository = userRedemptionRepository;
    }

    // =========================================================
    // GET ALL CODE REPORTS BY CLIENT ID
    // =========================================================

    public List<CodeReportResponse> getCodeReport(Long clientId) {

        return truvishCodeRepository
                .findByClientIdOrderByTruvishCodeTimestampDesc(clientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET CODE REPORTS BY STATUS
    // =========================================================

    public List<CodeReportResponse> getCodeReportByStatus(
            Long clientId,
            CodeReportStatus status
    ) {

        return truvishCodeRepository
                .findByClientIdOrderByTruvishCodeTimestampDesc(clientId)
                .stream()
                .filter(code -> calculateStatus(code) == status)
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET SINGLE CODE REPORT
    // =========================================================

    public CodeReportResponse getCodeReportByCode(
            String codeNumber
    ) {

        TruvishCode code =
                truvishCodeRepository
                        .findByTruvishIdCodeNumber(codeNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Code not found: " + codeNumber
                                )
                        );

        return mapToResponse(code);
    }


    // =========================================================
    // MAP TO RESPONSE
    // =========================================================

    private CodeReportResponse mapToResponse(
            TruvishCode code
    ) {

        LocalDateTime issuedDateTime =
                code.getTruvishCodeTimestamp();

        LocalDateTime expiryDate =
                calculateExpiryDate(
                        issuedDateTime,
                        code.getValidity()
                );

        CodeReportStatus reportStatus =
                calculateStatus(code);

        String originalCode =
                code.getTruvishIdCodeNumber();

        String redeemedBy =
                getRedeemedBy(originalCode);

        return new CodeReportResponse(

                // =================================================
                // MASKED CODE
                //
                // Example:
                // 813E-65DF-2E7D
                // ↓
                // 813E XXXX XX7D
                // =================================================
                maskCode(originalCode),

                // DENOMINATION
                code.getOriginalCodeValue(),

                // STATUS
                reportStatus.getLabel(),

                // ISSUED DATE & TIME
                issuedDateTime,

                // VALIDITY
                formatValidity(code.getValidity()),

                // EXPIRY DATE
                expiryDate,

                // CAMPAIGN NAME
                code.getClientTheme(),

                // THEME IMAGE
                code.getClientThemeImg(),

                // REDEEMED BY
                redeemedBy
        );
    }


    // =========================================================
    // MASK CODE
    //
    // Example:
    //
    // 813E-65DF-2E7D
    //        ↓
    // 813E XXXX XX7D
    //
    // Rule:
    // First 4 characters = visible
    // Middle 6 characters = XXXX XX
    // Last 2 characters = visible with last block
    // =========================================================

    private String maskCode(String code) {

        if (code == null || code.isBlank()) {
            return "-";
        }

        // Remove all separators
        String cleanCode =
                code.replaceAll("[^a-zA-Z0-9]", "");

        // Example:
        // 813E65DF2E7D
        //
        // Need at least 6 characters
        if (cleanCode.length() < 6) {
            return code;
        }

        // First 4 characters
        String firstPart =
                cleanCode.substring(0, 4);

        // Last 2 characters
        String lastPart =
                cleanCode.substring(
                        cleanCode.length() - 2
                );

        // Result:
        // 813E XXXX XX7D
        return firstPart + " XXXX XX" + lastPart;
    }


    // =========================================================
    // CALCULATE EXPIRY DATE
    // =========================================================

    private LocalDateTime calculateExpiryDate(
            LocalDateTime issuedDateTime,
            Integer validity
    ) {

        if (
                issuedDateTime == null
                        ||
                        validity == null
        ) {
            return null;
        }

        return issuedDateTime.plusMonths(validity);
    }


    // =========================================================
    // FORMAT VALIDITY
    // =========================================================

    private String formatValidity(
            Integer validity
    ) {

        if (validity == null) {
            return "N/A";
        }

        if (validity == 1) {
            return "1 Month";
        }

        return validity + " Months";
    }


    // =========================================================
    // CALCULATE STATUS
    //
    // PRIORITY:
    //
    // 1. REDEEMED
    // 2. EXPIRED BACK TO WALLET
    // 3. ACTIVE
    // =========================================================

    private CodeReportStatus calculateStatus(
            TruvishCode code
    ) {

        String codeNumber =
                code.getTruvishIdCodeNumber();


        // =====================================================
        // 1. REDEEMED
        // =====================================================

        if (
                code.getTruvishCodeStatus()
                        == VoucherStatus.REDEEMED
        ) {

            return CodeReportStatus.REDEEMED;
        }


        // =====================================================
        // CHECK REDEMPTION HISTORY
        //
        // Even if voucher status was not updated,
        // a redemption record means code is redeemed.
        // =====================================================

        if (
                codeNumber != null
                        &&
                        !codeNumber.isBlank()
        ) {

            List<UserRedemption> redemptions =
                    userRedemptionRepository
                            .findByUserTruvishCodeOrderByUserBrandTimeTempDesc(
                                    codeNumber
                            );

            if (
                    redemptions != null
                            &&
                            !redemptions.isEmpty()
            ) {

                return CodeReportStatus.REDEEMED;
            }
        }


        // =====================================================
        // 2. EXPIRED - BACK TO WALLET
        // =====================================================

        LocalDateTime expiryDate =
                calculateExpiryDate(
                        code.getTruvishCodeTimestamp(),
                        code.getValidity()
                );

        if (
                expiryDate != null
                        &&
                        !expiryDate.isAfter(
                                LocalDateTime.now()
                        )
        ) {

            return CodeReportStatus
                    .EXPIRED_BACK_TO_WALLET;
        }


        // =====================================================
        // 3. ACTIVE
        // =====================================================

        return CodeReportStatus.ACTIVE;
    }


    // =========================================================
    // GET REDEEMED BY
    //
    // IMPORTANT:
    // Uses ORIGINAL code, not masked code.
    // =========================================================

    private String getRedeemedBy(
            String codeNumber
    ) {

        if (
                codeNumber == null
                        ||
                        codeNumber.isBlank()
        ) {
            return null;
        }

        List<UserRedemption> redemptions =
                userRedemptionRepository
                        .findByUserTruvishCodeOrderByUserBrandTimeTempDesc(
                                codeNumber
                        );

        if (
                redemptions == null
                        ||
                        redemptions.isEmpty()
        ) {
            return null;
        }

        return redemptions
                .get(0)
                .getUserPhoneNumber();
    }
}