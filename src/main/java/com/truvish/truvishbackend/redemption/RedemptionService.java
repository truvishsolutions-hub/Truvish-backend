package com.truvish.truvishbackend.redemption;

import com.truvish.truvishbackend.TruBlankCode.TruBlankCode;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeRepository;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeStatus;

import com.truvish.truvishbackend.TruOpeAdmin.TruvishCode;
import com.truvish.truvishbackend.TruOpeAdmin.TruvishCodeRepository;
import com.truvish.truvishbackend.TruOpeAdmin.VoucherStatus;

import com.truvish.truvishbackend.voucherinventory.VoucherInventory;
import com.truvish.truvishbackend.voucherinventory.VoucherInventoryRepository;
import com.truvish.truvishbackend.voucherinventory.VoucherRedeemedLog;
import com.truvish.truvishbackend.voucherinventory.VoucherRedeemedLogRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RedemptionService {

    // =========================================================
    // REPOSITORIES
    // =========================================================

    private final TruvishCodeRepository codeRepo;

    private final TruBlankCodeRepository blankCodeRepo;

    private final VoucherInventoryRepository voucherInventoryRepo;

    private final VoucherRedeemedLogRepository voucherRedeemedLogRepo;

    private final UserRedemptionRepository userRepo;

    private final CodeRedemptionHistoryRepository codeRedemptionHistoryRepo;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public RedemptionService(
            TruvishCodeRepository codeRepo,
            TruBlankCodeRepository blankCodeRepo,
            VoucherInventoryRepository voucherInventoryRepo,
            VoucherRedeemedLogRepository voucherRedeemedLogRepo,
            UserRedemptionRepository userRepo,
            CodeRedemptionHistoryRepository codeRedemptionHistoryRepo
    ) {

        this.codeRepo = codeRepo;

        this.blankCodeRepo = blankCodeRepo;

        this.voucherInventoryRepo =
                voucherInventoryRepo;

        this.voucherRedeemedLogRepo =
                voucherRedeemedLogRepo;

        this.userRepo =
                userRepo;

        this.codeRedemptionHistoryRepo =
                codeRedemptionHistoryRepo;
    }

    // =========================================================
    // MAIN REDEEM METHOD
    // =========================================================

    public RedemptionResponse redeemAndReturnVoucher(
            String code,
            String phone,
            String brandName,
            Long selectedValue,
            String brandLogo
    ) {

        String normalizedCode =
                normalize(code);

        String normalizedPhone =
                normalize(phone);

        String normalizedBrandName =
                normalize(brandName);

        String normalizedBrandLogo =
                normalize(brandLogo);

        // =====================================================
        // VALIDATION
        // =====================================================

        if (
                normalizedCode == null ||
                        normalizedCode.isBlank()
        ) {

            throw new RuntimeException(
                    "Code is required"
            );
        }

        if (
                normalizedPhone == null ||
                        normalizedPhone.isBlank()
        ) {

            throw new RuntimeException(
                    "Phone is required"
            );
        }

        if (
                normalizedBrandName == null ||
                        normalizedBrandName.isBlank()
        ) {

            throw new RuntimeException(
                    "Brand name is required"
            );
        }

        if (
                selectedValue == null ||
                        selectedValue <= 0
        ) {

            throw new RuntimeException(
                    "Selected value must be greater than 0"
            );
        }

        // =====================================================
        // FIRST: DIGITAL TRUVISH CODE
        // =====================================================

        Optional<TruvishCode> truvishOptional =
                codeRepo.findByTruvishIdCodeNumber(
                        normalizedCode
                );

        if (
                truvishOptional.isPresent()
        ) {

            return redeemNormalTruvishCode(
                    truvishOptional.get(),
                    normalizedCode,
                    normalizedPhone,
                    normalizedBrandName,
                    selectedValue,
                    normalizedBrandLogo
            );
        }

        // =====================================================
        // SECOND: PHYSICAL TRUCARD
        // =====================================================

        Optional<TruBlankCode> blankCodeOptional =
                blankCodeRepo.findByCodeNumber(
                        normalizedCode
                );

        if (
                blankCodeOptional.isPresent()
        ) {

            return redeemBlankCode(
                    blankCodeOptional.get(),
                    normalizedCode,
                    normalizedPhone,
                    normalizedBrandName,
                    selectedValue,
                    normalizedBrandLogo
            );
        }

        // =====================================================
        // NOT FOUND
        // =====================================================

        throw new RuntimeException(
                "Invalid Code"
        );
    }

    // =========================================================
    // DIGITAL CODE REDEMPTION
    // =========================================================

    private RedemptionResponse redeemNormalTruvishCode(

            TruvishCode truvish,

            String normalizedCode,

            String normalizedPhone,

            String normalizedBrandName,

            Long selectedValue,

            String normalizedBrandLogo

    ) {

        // =====================================================
        // STATUS
        // =====================================================

        if (
                truvish.getTruvishCodeStatus()
                        != VoucherStatus.ACTIVE
        ) {

            throw new RuntimeException(
                    "Code is INACTIVE"
            );
        }

        // =====================================================
        // BALANCE
        // =====================================================

        Long currentBalance =
                truvish.getTruvishCodeValue();

        if (
                currentBalance == null ||
                        currentBalance <= 0
        ) {

            truvish.setTruvishCodeValue(
                    0L
            );

            truvish.setTruvishCodeStatus(
                    VoucherStatus.INACTIVE
            );

            codeRepo.save(
                    truvish
            );

            throw new RuntimeException(
                    "Code balance is 0"
            );
        }

        // =====================================================
        // SELECTED VALUE
        // =====================================================

        if (
                selectedValue > currentBalance
        ) {

            throw new RuntimeException(
                    "Selected value exceeds available balance"
            );
        }

        // =====================================================
        // PARTIAL REDEEM
        // =====================================================

        Boolean partialAllowed =
                truvish
                        .getTruvishIdIsPartialRedeemAllowed();

        if (
                Boolean.FALSE.equals(
                        partialAllowed
                )
                        &&
                        !selectedValue.equals(
                                currentBalance
                        )
        ) {

            throw new RuntimeException(
                    "Partial redeem is not allowed for this code"
            );
        }

        // =====================================================
        // BRAND VALIDATION
        // =====================================================

        validateBrandAllowedForNormalCode(
                truvish,
                normalizedBrandName
        );

        // =====================================================
        // FIND INVENTORY VOUCHER
        // =====================================================

        VoucherInventory voucher =
                findAvailableVoucher(
                        normalizedBrandName,
                        selectedValue
                );

        // =====================================================
        // BALANCE
        // =====================================================

        Long beforeBalance =
                currentBalance;

        Long afterBalance =
                currentBalance - selectedValue;

        LocalDateTime redeemedAt =
                LocalDateTime.now();

        String redeemStatus =
                afterBalance == 0
                        ? "FULL_REDEEM"
                        : "PARTIAL_REDEEM";

        String historyMessage =
                afterBalance == 0
                        ? "Code fully redeemed"
                        : "₹"
                        + selectedValue
                        + " redeemed successfully";

        // =====================================================
        // SAVE VOUCHER LOG
        // =====================================================

        saveVoucherRedeemedLog(
                voucher,
                redeemedAt,
                normalizedPhone,
                normalizedCode
        );

        // =====================================================
        // USER REDEMPTION
        // =====================================================

        UserRedemption user =
                new UserRedemption();

        user.setClientId(
                truvish.getClientId()
        );

        user.setClientCompanyName(
                truvish.getClientName()
        );

        user.setUserPhoneNumber(
                normalizedPhone
        );

        user.setUserTruvishCode(
                normalizedCode
        );

        user.setUserBrandName(
                voucher.getBrandName()
        );

        user.setUserBrandValue(
                selectedValue
        );

        user.setUserBrandVoucher(
                voucher.getVoucherCode()
        );

        user.setUserBrandPin(
                voucher.getVoucherPin()
        );

        user.setUserBrandValidity(
                voucher.getValidityTill()
        );

        user.setUserBrandTimeTemp(
                redeemedAt
        );

        user.setBeforeBalance(
                beforeBalance
        );

        user.setAfterBalance(
                afterBalance
        );

        user.setRedeemStatus(
                redeemStatus
        );

        user.setHistoryMessage(
                historyMessage
        );

        user.setBrandLogo(
                normalizedBrandLogo
        );

        user.setRedemptionProcess(
                voucher.getRedemptionProcess()
        );

        userRepo.save(
                user
        );

        // =====================================================
        // CODE REDEMPTION HISTORY
        // =====================================================

        CodeRedemptionHistory history =
                new CodeRedemptionHistory();

        history.setClientId(
                truvish.getClientId()
        );

        history.setClientCompanyName(
                truvish.getClientName()
        );

        history.setTruvishCode(
                normalizedCode
        );

        history.setPhoneNumber(
                normalizedPhone
        );

        history.setBrandName(
                voucher.getBrandName()
        );

        history.setRedeemedValue(
                selectedValue
        );

        history.setVoucherCode(
                voucher.getVoucherCode()
        );

        history.setVoucherPin(
                voucher.getVoucherPin()
        );

        history.setValidityTill(
                voucher.getValidityTill()
        );

        history.setBeforeBalance(
                beforeBalance
        );

        history.setAfterBalance(
                afterBalance
        );

        history.setRedeemStatus(
                redeemStatus
        );

        history.setHistoryMessage(
                historyMessage
        );

        history.setBrandLogo(
                normalizedBrandLogo
        );

        history.setRedemptionProcess(
                voucher.getRedemptionProcess()
        );

        history.setRedeemedAt(
                redeemedAt
        );

        codeRedemptionHistoryRepo.save(
                history
        );

        // =====================================================
        // MARK INVENTORY VOUCHER USED
        // =====================================================

        markVoucherAsUsed(
                voucher,
                redeemedAt,
                normalizedPhone,
                normalizedCode
        );

        // =====================================================
        // UPDATE DIGITAL CODE BALANCE
        // =====================================================

        truvish.setTruvishCodeValue(
                afterBalance
        );

        truvish.setTruvishCodeStatus(
                afterBalance == 0
                        ? VoucherStatus.INACTIVE
                        : VoucherStatus.ACTIVE
        );

        codeRepo.save(
                truvish
        );

        // =====================================================
        // RESPONSE
        // =====================================================

        return new RedemptionResponse(

                voucher.getBrandName(),

                selectedValue,

                voucher.getVoucherCode(),

                voucher.getVoucherPin(),

                voucher.getValidityTill(),

                beforeBalance,

                afterBalance,

                redeemStatus,

                historyMessage,

                redeemedAt
        );
    }

    // =========================================================
    // PHYSICAL TRUCARD REDEMPTION
    // =========================================================

    private RedemptionResponse redeemBlankCode(

            TruBlankCode blankCode,

            String normalizedCode,

            String normalizedPhone,

            String normalizedBrandName,

            Long selectedValue,

            String normalizedBrandLogo

    ) {

        // =====================================================
        // STATUS
        // =====================================================

        if (
                blankCode.getStatus()
                        != TruBlankCodeStatus.ACTIVE
        ) {

            throw new RuntimeException(
                    "Blank code is not ACTIVE"
            );
        }

        // =====================================================
        // EXPIRY
        // =====================================================

        if (
                blankCode.getExpiryDate() != null
                        &&
                        blankCode.getExpiryDate()
                                .isBefore(
                                        LocalDateTime.now()
                                )
        ) {

            throw new RuntimeException(
                    "Blank code has expired"
            );
        }

        // =====================================================
        // DENOMINATION
        // =====================================================

        Long currentBalance =
                blankCode.getDenomination();

        if (
                currentBalance == null ||
                        currentBalance <= 0
        ) {

            throw new RuntimeException(
                    "Blank code denomination is invalid"
            );
        }

        // =====================================================
        // FULL REDEEM ONLY
        // =====================================================

        if (
                !selectedValue.equals(
                        currentBalance
                )
        ) {

            throw new RuntimeException(
                    "For this code, selected value must be ₹"
                            + currentBalance
            );
        }

        // =====================================================
        // BRAND VALIDATION
        // =====================================================

        validateBrandAllowedForBlankCode(
                blankCode,
                normalizedBrandName
        );

        // =====================================================
        // FIND INVENTORY VOUCHER
        // =====================================================

        VoucherInventory voucher =
                findAvailableVoucher(
                        normalizedBrandName,
                        selectedValue
                );

        // =====================================================
        // REDEMPTION DATA
        // =====================================================

        Long beforeBalance =
                currentBalance;

        Long afterBalance =
                0L;

        LocalDateTime redeemedAt =
                LocalDateTime.now();

        String redeemStatus =
                "FULL_REDEEM";

        String historyMessage =
                "TruCard fully redeemed";

        // =====================================================
        // SAVE VOUCHER LOG
        // =====================================================

        saveVoucherRedeemedLog(
                voucher,
                redeemedAt,
                normalizedPhone,
                normalizedCode
        );

        // =====================================================
        // USER REDEMPTION
        //
        // IMPORTANT:
        // Physical TruCard already belongs to a Client.
        // Therefore clientId/clientName MUST be saved.
        // =====================================================

        UserRedemption user =
                new UserRedemption();

        user.setClientId(
                blankCode.getClientId()
        );

        user.setClientCompanyName(
                blankCode.getClientName()
        );

        user.setUserPhoneNumber(
                normalizedPhone
        );

        user.setUserTruvishCode(
                normalizedCode
        );

        user.setUserBrandName(
                voucher.getBrandName()
        );

        user.setUserBrandValue(
                selectedValue
        );

        user.setUserBrandVoucher(
                voucher.getVoucherCode()
        );

        user.setUserBrandPin(
                voucher.getVoucherPin()
        );

        user.setUserBrandValidity(
                voucher.getValidityTill()
        );

        user.setUserBrandTimeTemp(
                redeemedAt
        );

        user.setBeforeBalance(
                beforeBalance
        );

        user.setAfterBalance(
                afterBalance
        );

        user.setRedeemStatus(
                redeemStatus
        );

        user.setHistoryMessage(
                historyMessage
        );

        user.setBrandLogo(
                normalizedBrandLogo
        );

        user.setRedemptionProcess(
                voucher.getRedemptionProcess()
        );

        userRepo.save(
                user
        );

        // =====================================================
        // CODE REDEMPTION HISTORY
        // =====================================================

        CodeRedemptionHistory history =
                new CodeRedemptionHistory();

        history.setClientId(
                blankCode.getClientId()
        );

        history.setClientCompanyName(
                blankCode.getClientName()
        );

        history.setTruvishCode(
                normalizedCode
        );

        history.setPhoneNumber(
                normalizedPhone
        );

        history.setBrandName(
                voucher.getBrandName()
        );

        history.setRedeemedValue(
                selectedValue
        );

        history.setVoucherCode(
                voucher.getVoucherCode()
        );

        history.setVoucherPin(
                voucher.getVoucherPin()
        );

        history.setValidityTill(
                voucher.getValidityTill()
        );

        history.setBeforeBalance(
                beforeBalance
        );

        history.setAfterBalance(
                afterBalance
        );

        history.setRedeemStatus(
                redeemStatus
        );

        history.setHistoryMessage(
                historyMessage
        );

        history.setBrandLogo(
                normalizedBrandLogo
        );

        history.setRedemptionProcess(
                voucher.getRedemptionProcess()
        );

        history.setRedeemedAt(
                redeemedAt
        );

        codeRedemptionHistoryRepo.save(
                history
        );

        // =====================================================
        // MARK INVENTORY VOUCHER USED
        // =====================================================

        markVoucherAsUsed(
                voucher,
                redeemedAt,
                normalizedPhone,
                normalizedCode
        );

        // =====================================================
        // MARK TRUCARD REDEEMED
        // =====================================================

        blankCode.setStatus(
                TruBlankCodeStatus.REDEEMED
        );

        blankCode.setRedeemedAt(
                redeemedAt
        );

        blankCode.setUpdatedAt(
                redeemedAt
        );

        blankCodeRepo.save(
                blankCode
        );

        // =====================================================
        // RESPONSE
        // =====================================================

        return new RedemptionResponse(

                voucher.getBrandName(),

                selectedValue,

                voucher.getVoucherCode(),

                voucher.getVoucherPin(),

                voucher.getValidityTill(),

                beforeBalance,

                afterBalance,

                redeemStatus,

                historyMessage,

                redeemedAt
        );
    }

    // =========================================================
    // FIND AVAILABLE INVENTORY VOUCHER
    // =========================================================

    private VoucherInventory findAvailableVoucher(

            String brandName,

            Long selectedValue

    ) {

        BigDecimal denomination =
                BigDecimal.valueOf(
                        selectedValue
                );

        return voucherInventoryRepo
                .findFirstByBrandNameIgnoreCaseAndDenominationAndStatusIgnoreCaseAndValidityTillGreaterThanEqualOrderByValidityTillAscCreatedAtAsc(

                        brandName,

                        denomination,

                        "ACTIVE",

                        LocalDate.now()

                )
                .orElseThrow(() ->
                        new RuntimeException(

                                "No available active voucher found for brand "
                                        + brandName
                                        + " and denomination "
                                        + selectedValue

                        )
                );
    }

    // =========================================================
    // SAVE VOUCHER REDEEMED LOG
    // =========================================================

    private void saveVoucherRedeemedLog(

            VoucherInventory voucher,

            LocalDateTime redeemedAt,

            String phone,

            String code

    ) {

        VoucherRedeemedLog log =
                new VoucherRedeemedLog();

        log.setInventoryId(
                voucher.getId()
        );

        log.setBrandName(
                voucher.getBrandName()
        );

        log.setDenomination(
                voucher.getDenomination()
        );

        log.setVoucherCode(
                voucher.getVoucherCode()
        );

        log.setVoucherPin(
                voucher.getVoucherPin()
        );

        log.setValidityTill(
                voucher.getValidityTill()
        );

        log.setRedeemedAt(
                redeemedAt
        );

        log.setRedeemedBy(
                phone
        );

        log.setOrderReference(
                code
        );

        voucherRedeemedLogRepo.save(
                log
        );
    }

    // =========================================================
    // MARK INVENTORY VOUCHER USED
    // =========================================================

    private void markVoucherAsUsed(

            VoucherInventory voucher,

            LocalDateTime redeemedAt,

            String phone,

            String code

    ) {

        voucher.setStatus(
                "USED"
        );

        voucher.setUsedAt(
                redeemedAt
        );

        voucher.setUsedBy(
                phone
        );

        voucher.setUsedOrderReference(
                code
        );

        voucherInventoryRepo.save(
                voucher
        );
    }

    // =========================================================
    // NORMAL DIGITAL CODE BRAND VALIDATION
    // =========================================================

    private void validateBrandAllowedForNormalCode(

            TruvishCode truvish,

            String brandName

    ) {

        List<String> allowedBrands =
                truvish.getClientBrand();

        if (
                allowedBrands == null
                        ||
                        allowedBrands.isEmpty()
        ) {

            throw new RuntimeException(
                    "No brand is assigned to this code"
            );
        }

        boolean matched =
                allowedBrands
                        .stream()
                        .filter(
                                value ->
                                        value != null
                                                &&
                                                !value.trim().isBlank()
                        )
                        .anyMatch(
                                value ->
                                        value
                                                .trim()
                                                .equalsIgnoreCase(
                                                        brandName
                                                )
                        );

        if (!matched) {

            throw new RuntimeException(
                    "Selected brand is not allowed for this code"
            );
        }
    }

    // =========================================================
    // PHYSICAL TRUCARD BRAND VALIDATION
    // =========================================================

    private void validateBrandAllowedForBlankCode(

            TruBlankCode blankCode,

            String brandName

    ) {

        List<String> allowedBrands =
                blankCode.getBrandNames();

        if (
                allowedBrands == null
                        ||
                        allowedBrands.isEmpty()
        ) {

            throw new RuntimeException(
                    "No brand is assigned to this blank code"
            );
        }

        boolean matched =
                allowedBrands
                        .stream()
                        .filter(
                                value ->
                                        value != null
                                                &&
                                                !value.trim().isBlank()
                        )
                        .anyMatch(
                                value ->
                                        value
                                                .trim()
                                                .equalsIgnoreCase(
                                                        brandName
                                                )
                        );

        if (!matched) {

            throw new RuntimeException(
                    "Selected brand is not allowed for this blank code"
            );
        }
    }

    // =========================================================
    // NORMALIZE
    // =========================================================

    private String normalize(
            String value
    ) {

        return value == null
                ? null
                : value.trim();
    }
}