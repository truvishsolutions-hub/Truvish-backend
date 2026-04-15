package com.truvish.truvishbackend.redemption;

import com.truvish.truvishbackend.TruvishCode.TruvishCode;
import com.truvish.truvishbackend.TruvishCode.TruvishCodeRepository;
import com.truvish.truvishbackend.voucherinventory.VoucherInventory;
import com.truvish.truvishbackend.voucherinventory.VoucherInventoryRepository;
import com.truvish.truvishbackend.voucherinventory.VoucherRedeemedLog;
import com.truvish.truvishbackend.voucherinventory.VoucherRedeemedLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@Transactional
public class RedemptionService {

    private final TruvishCodeRepository codeRepo;
    private final VoucherInventoryRepository voucherInventoryRepo;
    private final VoucherRedeemedLogRepository voucherRedeemedLogRepo;
    private final UserRedemptionRepository userRepo;
    private final CodeRedemptionHistoryRepository codeRedemptionHistoryRepo;

    public RedemptionService(
            TruvishCodeRepository codeRepo,
            VoucherInventoryRepository voucherInventoryRepo,
            VoucherRedeemedLogRepository voucherRedeemedLogRepo,
            UserRedemptionRepository userRepo,
            CodeRedemptionHistoryRepository codeRedemptionHistoryRepo
    ) {
        this.codeRepo = codeRepo;
        this.voucherInventoryRepo = voucherInventoryRepo;
        this.voucherRedeemedLogRepo = voucherRedeemedLogRepo;
        this.userRepo = userRepo;
        this.codeRedemptionHistoryRepo = codeRedemptionHistoryRepo;
    }

    public RedemptionResponse redeemAndReturnVoucher(
            String code,
            String phone,
            String brandName,
            Long selectedValue,
            String brandLogo
    ) {
        String normalizedCode = normalize(code);
        String normalizedPhone = normalize(phone);
        String normalizedBrandName = normalize(brandName);
        String normalizedBrandLogo = normalize(brandLogo);

        if (normalizedCode == null || normalizedCode.isBlank()) {
            throw new RuntimeException("Code is required");
        }

        if (normalizedPhone == null || normalizedPhone.isBlank()) {
            throw new RuntimeException("Phone is required");
        }

        if (normalizedBrandName == null || normalizedBrandName.isBlank()) {
            throw new RuntimeException("Brand name is required");
        }

        if (selectedValue == null || selectedValue <= 0) {
            throw new RuntimeException("Selected value must be greater than 0");
        }

        TruvishCode truvish = codeRepo.findByTruvishIdCodeNumber(normalizedCode)
                .orElseThrow(() -> new RuntimeException("Invalid Code"));

        if (!"ACTIVE".equalsIgnoreCase(truvish.getTruvishCodeStatus())) {
            throw new RuntimeException("Code is INACTIVE");
        }

        Long currentBalance = truvish.getTruvishCodeValue();
        if (currentBalance == null || currentBalance <= 0) {
            truvish.setTruvishCodeValue(0L);
            truvish.setTruvishCodeStatus("INACTIVE");
            codeRepo.save(truvish);
            throw new RuntimeException("Code balance is 0");
        }

        if (selectedValue > currentBalance) {
            throw new RuntimeException("Selected value exceeds available balance");
        }

        Boolean partialAllowed = truvish.getTruvishIdIsPartialRedeemAllowed();
        if (Boolean.FALSE.equals(partialAllowed) && !selectedValue.equals(currentBalance)) {
            throw new RuntimeException("Partial redeem is not allowed for this code");
        }

        validateBrandAllowedForClient(truvish, normalizedBrandName);

        BigDecimal denomination = BigDecimal.valueOf(selectedValue);

        VoucherInventory voucher = voucherInventoryRepo
                .findFirstByBrandNameIgnoreCaseAndDenominationAndStatusIgnoreCaseAndValidityTillGreaterThanEqualOrderByValidityTillAscCreatedAtAsc(
                        normalizedBrandName,
                        denomination,
                        "ACTIVE",
                        LocalDate.now()
                )
                .orElseThrow(() -> new RuntimeException(
                        "No available active voucher found for brand "
                                + normalizedBrandName
                                + " and denomination "
                                + selectedValue
                ));

        Long beforeBalance = currentBalance;
        Long afterBalance = currentBalance - selectedValue;
        LocalDateTime redeemedAt = LocalDateTime.now();

        String redeemStatus = afterBalance == 0 ? "FULL_REDEEM" : "PARTIAL_REDEEM";
        String historyMessage = afterBalance == 0
                ? "Code fully redeemed"
                : "₹" + selectedValue + " redeemed successfully";

        VoucherRedeemedLog log = new VoucherRedeemedLog();
        log.setInventoryId(voucher.getId());
        log.setBrandName(voucher.getBrandName());
        log.setDenomination(voucher.getDenomination());
        log.setVoucherCode(voucher.getVoucherCode());
        log.setVoucherPin(voucher.getVoucherPin());
        log.setValidityTill(voucher.getValidityTill());
        log.setRedeemedAt(redeemedAt);
        log.setRedeemedBy(normalizedPhone);
        log.setOrderReference(normalizedCode);
        voucherRedeemedLogRepo.save(log);

        UserRedemption user = new UserRedemption();
        user.setClientId(truvish.getClientId());
        user.setClientCompanyName(truvish.getClientName());
        user.setUserPhoneNumber(normalizedPhone);
        user.setUserTruvishCode(normalizedCode);
        user.setUserBrandName(voucher.getBrandName());
        user.setUserBrandValue(selectedValue);
        user.setUserBrandVoucher(voucher.getVoucherCode());
        user.setUserBrandPin(voucher.getVoucherPin());
        user.setUserBrandValidity(voucher.getValidityTill());
        user.setUserBrandTimeTemp(redeemedAt);
        user.setBeforeBalance(beforeBalance);
        user.setAfterBalance(afterBalance);
        user.setRedeemStatus(redeemStatus);
        user.setHistoryMessage(historyMessage);
        user.setBrandLogo(normalizedBrandLogo);
        user.setRedemptionProcess(voucher.getRedemptionProcess());
        userRepo.save(user);

        CodeRedemptionHistory history = new CodeRedemptionHistory();
        history.setClientId(truvish.getClientId());
        history.setClientCompanyName(truvish.getClientName());
        history.setTruvishCode(normalizedCode);
        history.setPhoneNumber(normalizedPhone);
        history.setBrandName(voucher.getBrandName());
        history.setRedeemedValue(selectedValue);
        history.setVoucherCode(voucher.getVoucherCode());
        history.setVoucherPin(voucher.getVoucherPin());
        history.setValidityTill(voucher.getValidityTill());
        history.setBeforeBalance(beforeBalance);
        history.setAfterBalance(afterBalance);
        history.setRedeemStatus(redeemStatus);
        history.setHistoryMessage(historyMessage);
        history.setBrandLogo(normalizedBrandLogo);
        history.setRedemptionProcess(voucher.getRedemptionProcess());
        history.setRedeemedAt(redeemedAt);
        codeRedemptionHistoryRepo.save(history);

        voucher.setStatus("USED");
        voucher.setUsedAt(redeemedAt);
        voucher.setUsedBy(normalizedPhone);
        voucher.setUsedOrderReference(normalizedCode);
        voucherInventoryRepo.save(voucher);

        truvish.setTruvishCodeValue(afterBalance);
        truvish.setTruvishCodeStatus(afterBalance == 0 ? "INACTIVE" : "ACTIVE");
        codeRepo.save(truvish);

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

    private void validateBrandAllowedForClient(TruvishCode truvish, String brandName) {
        String[] allowedBrands = truvish.getClientBrand();

        if (allowedBrands == null || allowedBrands.length == 0) {
            throw new RuntimeException("No brand is assigned to this code");
        }

        boolean matched = Arrays.stream(allowedBrands)
                .filter(v -> v != null && !v.trim().isBlank())
                .anyMatch(v -> v.trim().equalsIgnoreCase(brandName));

        if (!matched) {
            throw new RuntimeException("Selected brand is not allowed for this code");
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }
}