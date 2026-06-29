package com.truvish.truvishbackend.TruvishCode;

import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;
import org.springframework.stereotype.Service;
import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

import com.truvish.truvishbackend.TruvishCode.VoucherStatus;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
//UUID import
import java.util.UUID;

@Service
public class TruvishCodeService {

    private final TruvishCodeRepository repo;
    private final UserRedemptionRepository userRepo;
    private final WalletTransactionService walletService;
    private final ClientRepository clientRepo;



    public TruvishCodeService(
            TruvishCodeRepository repo,
            UserRedemptionRepository userRepo,
            WalletTransactionService walletService,
            ClientRepository clientRepo
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.walletService = walletService;
        this.clientRepo = clientRepo;
    }
    // ======================================
// GENERATE CODE (14 CHAR FORMAT)
// Example: GIFT-9X2P-LM8Q
// ======================================
    private String generateCode() {

        String uuid = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();

        return String.format("%s-%s-%s",
                uuid.substring(0, 4),
                uuid.substring(4, 8),
                uuid.substring(8, 12)
        );
    }

    // ======================================
// ENSURE UNIQUE CODE
// ======================================
    private String generateUniqueCode() {

        String code;

        do {
            code = generateCode();
        } while (repo.existsByTruvishIdCodeNumber(code));

        return code;
    }

    public TruvishCode create(TruvishCode code) {
        code.setTruvishCodeTimestamp(LocalDateTime.now());

        Long value = code.getTruvishCodeValue();
        if (value != null && value > 0) {
            code.setTruvishCodeStatus(VoucherStatus.ACTIVE);
        } else {
            code.setTruvishCodeStatus(VoucherStatus.INACTIVE);
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

        VoucherStatus status =
                currentBalance > 0
                        ? VoucherStatus.ACTIVE
                        : VoucherStatus.INACTIVE;

        if (code.getTruvishCodeStatus() != status) {
            code.setTruvishCodeStatus(status);
            repo.save(code);
        }

        return new VerifyCodeResponse(
                codeNumber,
                status.name(),
                null,
                currentBalance,
                code.getClientImg(),
                code.getValidity(),
                code.getClientThemeImg(),
                code.getClientBrand(),
                code.getClientCategory()
        );
    }

    public List<ClientHistoryItem> history(
            String clientName,
            int page,
            int size
    ) {

        List<TruvishCode> codes =
                repo.findByClientNameIgnoreCaseOrderByTruvishCodeTimestampDesc(clientName);
        System.out.println("ClientName = " + clientName);
        System.out.println("Codes found = " + codes.size());


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
        System.out.println("Items found = " + items.size());
        return items;
    }

    @Transactional
    public List<TruvishCode> updateClient(
            CodeAssignmentDto dto
    ) {

        if (dto.getClientId() == null) {
            throw new RuntimeException("ClientId required");
        }

        if (dto.getTruvishCodeValue() == null ||
                dto.getTruvishCodeValue() <= 0) {
            throw new RuntimeException("Voucher value required");
        }

        Integer quantity = dto.getQuantity();

        if (quantity == null || quantity < 1) {
            quantity = 1;
        }

// ======================
// WALLET BALANCE CHECK
// ======================

        Client client = clientRepo.findById(dto.getClientId())
                .orElseThrow(() ->
                        new RuntimeException("Client not found")
                );

        BigDecimal totalAmount =
                BigDecimal.valueOf(dto.getTruvishCodeValue())
                        .multiply(BigDecimal.valueOf(quantity));



        BigDecimal balance =
                client.getBalance() == null
                        ? BigDecimal.ZERO
                        : client.getBalance();

        if (balance.compareTo(totalAmount) < 0) {

            throw new RuntimeException(
                    "Insufficient Balance. Required: "
                            + totalAmount
                            + ", Available: "
                            + balance
            );
        }

        List<TruvishCode> savedCodes = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {

            TruvishCode code = new TruvishCode();

            code.setTruvishIdCodeNumber(
                    generateUniqueCode()
            );

            code.setClientId(dto.getClientId());
            code.setClientName(dto.getClientName());

            code.setTruvishCodeValue(
                    dto.getTruvishCodeValue()
            );

            code.setOriginalCodeValue(
                    dto.getTruvishCodeValue()
            );

            code.setClientTheme(
                    dto.getClientTheme()
            );

            code.setClientThemeImg(
                    dto.getClientThemeImg()
            );

            code.setClientBrand(
                    dto.getClientBrand()
            );

            code.setClientCategory(
                    dto.getClientCategory()
            );

            code.setClientImg(
                    dto.getClientImg()
            );

            code.setValidity(
                    dto.getValidity()
            );

            code.setTruvishCodeTimestamp(
                    LocalDateTime.now()
            );

            code.setTruvishCodeStatus(VoucherStatus.ACTIVE);

            TruvishCode saved =
                    repo.save(code);

            savedCodes.add(saved);
        }

        // Wallet se total amount debit
        CreateWalletTxnRequest walletReq =
                new CreateWalletTxnRequest();

        walletReq.setAmount(
                BigDecimal.valueOf(
                        dto.getTruvishCodeValue() * quantity
                )
        );

        walletReq.setType("DEBIT");
        walletReq.setDescription(
                "Voucher Purchase (" + quantity + " Codes)"
        );

        walletReq.setReferenceType(
                "VOUCHER"
        );

        walletReq.setReferenceId(
                UUID.randomUUID().toString()
        );

        walletService.create(
                dto.getClientId(),
                walletReq
        );

        return savedCodes;
    }
}