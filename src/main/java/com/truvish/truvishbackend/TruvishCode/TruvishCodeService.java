package com.truvish.truvishbackend.TruvishCode;

import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TruvishCodeService {

    private final TruvishCodeRepository repo;
    private final UserRedemptionRepository userRepo;
    private final WalletTransactionService walletService;

    public TruvishCodeService(
            TruvishCodeRepository repo,
            UserRedemptionRepository userRepo,
            WalletTransactionService walletService
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.walletService = walletService;
    }

    public TruvishCode create(TruvishCode code) {
        code.setTruvishCodeTimestamp(LocalDateTime.now());

        Long value = code.getTruvishCodeValue();
        if (value != null && value > 0) {
            code.setTruvishCodeStatus("ACTIVE");
        } else {
            code.setTruvishCodeStatus("INACTIVE");
        }

        if (code.getOriginalCodeValue() == null) {
            code.setOriginalCodeValue(code.getTruvishCodeValue());
        }

        return repo.save(code);
    }

    public VerifyCodeResponse verifyCode(String codeNumber) {
        TruvishCode code = repo.findByTruvishIdCodeNumber(codeNumber)
                .orElseThrow(() -> new RuntimeException("Invalid Code"));

        Long currentBalance = code.getTruvishCodeValue();
        if (currentBalance == null) {
            currentBalance = 0L;
        }

        String status = currentBalance > 0 ? "ACTIVE" : "INACTIVE";

        if (!status.equalsIgnoreCase(code.getTruvishCodeStatus())) {
            code.setTruvishCodeStatus(status);
            repo.save(code);
        }

        return new VerifyCodeResponse(
                codeNumber,
                status,
                null,
                currentBalance,
                code.getClientImg(),
                code.getValidity(),
                code.getClientThemeImg(),
                code.getClientBrand(),
                code.getClientCategory()
        );
    }

    public List<ClientHistoryItem> history(String clientName) {

        List<TruvishCode> codes =
                repo.findByClientNameOrderByTruvishCodeTimestampDesc(clientName);

        List<ClientHistoryItem> items = new ArrayList<>();

        for (TruvishCode code : codes) {

            Long originalAmount =
                    code.getOriginalCodeValue() != null
                            ? code.getOriginalCodeValue()
                            : code.getTruvishCodeValue();

            LocalDateTime expiryDate = null;

            if (code.getTruvishCodeTimestamp() != null
                    && code.getValidity() != null) {

                expiryDate =
                        code.getTruvishCodeTimestamp()
                                .plusMonths(code.getValidity());
            }

            // ======================================
            // CODE ASSIGNED
            // ======================================

            items.add(
                    new ClientHistoryItem(
                            code.getTruvishCodeTimestamp(),
                            code.getTruvishIdCodeNumber(),
                            originalAmount,
                            code.getTruvishCodeValue(),
                            "Code assigned",
                            "CODE_ASSIGNED",
                            code.getValidity(),
                            expiryDate,
                            "-", // redeemedBrand
                            "-", // redeemedPhone
                            code.getTruvishCodeTimestamp(),
                            null // redeemedDate
                    )
            );

            // ======================================
            // REDEMPTIONS
            // ======================================

            List<UserRedemption> redemptions =
                    userRepo.findByUserTruvishCodeOrderByUserBrandTimeTempAsc(
                            code.getTruvishIdCodeNumber()
                    );

            for (UserRedemption redemption : redemptions) {

                items.add(
                        new ClientHistoryItem(
                                redemption.getUserBrandTimeTemp(),
                                redemption.getUserTruvishCode(),
                                redemption.getUserBrandValue(),
                                redemption.getAfterBalance(),
                                redemption.getHistoryMessage(),
                                redemption.getRedeemStatus(),
                                code.getValidity(),
                                expiryDate,

                                // NEW
                                redemption.getUserBrandName(),

                                // NEW
                                redemption.getUserPhoneNumber(),

                                // NEW
                                code.getTruvishCodeTimestamp(),

                                // NEW
                                redemption.getUserBrandTimeTemp()
                        )
                );
            }
        }

        items.sort(
                Comparator.comparing(ClientHistoryItem::getEventTime)
                        .reversed()
        );

        return items;
    }

    public TruvishCode updateClient(CodeAssignmentDto dto) {
        if (dto.getClientId() == null) {
            throw new RuntimeException("ClientId required");
        }

        if (dto.getTruvishCodeValue() == null || dto.getTruvishCodeValue() <= 0) {
            throw new RuntimeException("Voucher value required");
        }

        TruvishCode code = repo.findEmptyRow()
                .orElseThrow(() -> new RuntimeException("No empty code available"));

        code.setClientId(dto.getClientId());
        code.setClientName(dto.getClientName());
        code.setTruvishCodeValue(dto.getTruvishCodeValue());
        code.setOriginalCodeValue(dto.getTruvishCodeValue());
        code.setClientTheme(dto.getClientTheme());
        code.setClientThemeImg(dto.getClientThemeImg());
        code.setClientBrand(dto.getClientBrand());
        code.setClientCategory(dto.getClientCategory());
        code.setClientImg(dto.getClientImg());
        code.setValidity(dto.getValidity());
        code.setTruvishCodeTimestamp(LocalDateTime.now());
        code.setTruvishCodeStatus(dto.getTruvishCodeValue() > 0 ? "ACTIVE" : "INACTIVE");

        TruvishCode saved = repo.save(code);

        CreateWalletTxnRequest walletReq = new CreateWalletTxnRequest();
        walletReq.setAmount(BigDecimal.valueOf(dto.getTruvishCodeValue()));
        walletReq.setType("DEBIT");
        walletReq.setDescription("Voucher Purchase");
        walletReq.setReferenceType("VOUCHER");
        walletReq.setReferenceId(saved.getTruvishIdCodeNumber());

        walletService.create(dto.getClientId(), walletReq);

        return saved;
    }
}