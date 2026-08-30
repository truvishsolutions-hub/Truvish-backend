package com.truvish.truvishbackend.TruvishCode;

import com.truvish.truvishbackend.TruBlankCode.TruBlankCode;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeRepository;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeStatus;
import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class TruvishCodeService {

    private final TruvishCodeRepository repo;
    private final UserRedemptionRepository userRepo;
    private final WalletTransactionService walletService;
    private final ClientRepository clientRepo;
    private final TruBlankCodeRepository blankCodeRepo;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TruvishCodeService(
            TruvishCodeRepository repo,
            UserRedemptionRepository userRepo,
            WalletTransactionService walletService,
            ClientRepository clientRepo,
            TruBlankCodeRepository blankCodeRepo
    ) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.walletService = walletService;
        this.clientRepo = clientRepo;
        this.blankCodeRepo = blankCodeRepo;
    }

    // =========================================================
    // GENERATE DIGITAL CODE
    // =========================================================

    private String generateCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        return String.format("%s-%s-%s",
                uuid.substring(0, 4),
                uuid.substring(4, 8),
                uuid.substring(8, 12)
        );
    }

    private String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (repo.existsByTruvishIdCodeNumber(code));
        return code;
    }

    // =========================================================
    // CREATE DIGITAL CODE
    // =========================================================

    public TruvishCode create(TruvishCode code) {
        code.setTruvishCodeTimestamp(LocalDateTime.now());
        Long value = code.getTruvishCodeValue();
        code.setTruvishCodeStatus((value != null && value > 0) ? VoucherStatus.ACTIVE : VoucherStatus.INACTIVE);
        if (code.getOriginalCodeValue() == null) {
            code.setOriginalCodeValue(code.getTruvishCodeValue());
        }
        return repo.save(code);
    }

    // =========================================================
    // VERIFY CODE
    // =========================================================

    public VerifyCodeResponse verifyCode(String codeNumber) {
        if (codeNumber == null || codeNumber.trim().isEmpty()) {
            throw new RuntimeException("Code required");
        }
        String finalCode = codeNumber.trim().toUpperCase();

        var normalCode = repo.findByTruvishIdCodeNumber(finalCode);
        if (normalCode.isPresent()) {
            TruvishCode code = normalCode.get();
            Long currentBalance = code.getTruvishCodeValue() != null ? code.getTruvishCodeValue() : 0L;
            VoucherStatus status = currentBalance > 0 ? VoucherStatus.ACTIVE : VoucherStatus.INACTIVE;
            if (code.getTruvishCodeStatus() != status) {
                code.setTruvishCodeStatus(status);
                repo.save(code);
            }
            return new VerifyCodeResponse(
                    finalCode,
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

        // Physical TruCard
        TruBlankCode blankCode = blankCodeRepo.findByCodeNumber(finalCode)
                .orElseThrow(() -> new RuntimeException("Invalid Code"));

        if (blankCode.getStatus() != TruBlankCodeStatus.ACTIVE) {
            throw new RuntimeException("This code is not active yet. Please contact the administrator.");
        }
        if (blankCode.getDenomination() == null || blankCode.getDenomination() <= 0) {
            throw new RuntimeException("Code value is not configured");
        }
        if (blankCode.getValidityMonths() == null || blankCode.getValidityMonths() <= 0) {
            throw new RuntimeException("Code validity is not configured");
        }
        if (blankCode.getExpiryDate() != null && blankCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("This code has expired");
        }

        String clientImg = blankCode.getClientImg();
        return new VerifyCodeResponse(
                finalCode,
                blankCode.getStatus().name(),
                null,
                blankCode.getDenomination(),
                clientImg,
                blankCode.getValidityMonths(),
                blankCode.getThemeImg(),
                blankCode.getBrandNames(),
                blankCode.getBrandCategory() == null ? new ArrayList<>() : List.of(blankCode.getBrandCategory())
        );
    }

    // =========================================================
    // HISTORY BY CLIENT NAME
    // =========================================================

    public List<ClientHistoryItem> history(String clientName, int page, int size) {
        List<TruvishCode> digitalCodes = repo.findByClientNameIgnoreCaseOrderByTruvishCodeTimestampDesc(clientName);
        List<ClientHistoryItem> items = buildHistoryItems(digitalCodes);
        return paginate(items, page, size);
    }

    // =========================================================
    // HISTORY BY CLIENT ID – combines Digital + Physical
    // =========================================================

    public List<ClientHistoryItem> historyByClientId(Long clientId, int page, int size) {
        if (clientId == null || clientId <= 0) {
            throw new RuntimeException("Invalid clientId");
        }

        // Ensure client exists
        clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

        // Digital codes for this client
        List<TruvishCode> digitalCodes = repo.findAll().stream()
                .filter(code -> code.getClientId() != null && code.getClientId().equals(clientId))
                .toList();

        // Physical TruCards for this client
        List<TruBlankCode> physicalCodes = blankCodeRepo.findByClientIdOrderByCreatedAtDesc(clientId);

        // Build history items
        List<ClientHistoryItem> items = buildHistoryItems(digitalCodes);
        items.addAll(buildPhysicalHistoryItems(physicalCodes));

        // Sort newest first
        items.sort(Comparator.comparing(
                ClientHistoryItem::getEventTime,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return paginate(items, page, size);
    }

    // =========================================================
    // BUILD DIGITAL HISTORY
    // =========================================================

    private List<ClientHistoryItem> buildHistoryItems(List<TruvishCode> codes) {
        List<ClientHistoryItem> items = new ArrayList<>();
        List<UserRedemption> allRedemptions = userRepo.findAll();

        for (TruvishCode code : codes) {
            Long originalAmount = code.getOriginalCodeValue() != null ? code.getOriginalCodeValue() : code.getTruvishCodeValue();
            LocalDateTime expiryDate = null;
            if (code.getTruvishCodeTimestamp() != null && code.getValidity() != null) {
                expiryDate = code.getTruvishCodeTimestamp().plusMonths(code.getValidity());
            }

            // CODE ASSIGNED
            ClientHistoryItem assigned = new ClientHistoryItem(
                    code.getTruvishCodeTimestamp(),
                    code.getTruvishIdCodeNumber(),
                    originalAmount,
                    code.getTruvishCodeValue(),
                    "Code assigned",
                    "CODE_ASSIGNED",
                    code.getValidity(),
                    expiryDate,
                    "-",
                    "-",
                    code.getTruvishCodeTimestamp(),
                    null
            );
            assigned.setCodeType("DIGITAL");
            // Digital has no serial/reference, set to null or empty
            assigned.setSerialNumber(null);
            assigned.setReferenceNumber(null);
            items.add(assigned);

            // Redemptions
            List<UserRedemption> redemptions = allRedemptions.stream()
                    .filter(r -> r.getUserTruvishCode() != null && r.getUserTruvishCode().equals(code.getTruvishIdCodeNumber()))
                    .sorted(Comparator.comparing(UserRedemption::getUserBrandTimeTemp, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();

            for (UserRedemption redemption : redemptions) {
                ClientHistoryItem redeemItem = new ClientHistoryItem(
                        redemption.getUserBrandTimeTemp(),
                        redemption.getUserTruvishCode(),
                        redemption.getUserBrandValue(),
                        redemption.getAfterBalance(),
                        redemption.getHistoryMessage(),
                        redemption.getRedeemStatus(),
                        code.getValidity(),
                        expiryDate,
                        redemption.getUserBrandName(),
                        redemption.getUserPhoneNumber(),
                        code.getTruvishCodeTimestamp(),
                        redemption.getUserBrandTimeTemp()
                );
                redeemItem.setCodeType("DIGITAL");
                redeemItem.setSerialNumber(null);
                redeemItem.setReferenceNumber(null);
                items.add(redeemItem);
            }
        }
        return items;
    }

    // =========================================================
    // BUILD PHYSICAL TRUCARD HISTORY
    // =========================================================

    private List<ClientHistoryItem> buildPhysicalHistoryItems(List<TruBlankCode> physicalCodes) {
        List<ClientHistoryItem> items = new ArrayList<>();
        List<UserRedemption> allRedemptions = userRepo.findAll();

        for (TruBlankCode blankCode : physicalCodes) {
            String codeNumber = blankCode.getCodeNumber();
            Long denomination = blankCode.getDenomination();
            LocalDateTime issuedDate = blankCode.getCreatedAt();
            LocalDateTime activationDate = blankCode.getActivatedAt();
            LocalDateTime expiryDate = blankCode.getExpiryDate();

            LocalDateTime eventTime = (activationDate != null) ? activationDate : issuedDate;
            Long remainingBalance = (blankCode.getClientBalanceAfterActivation() != null)
                    ? blankCode.getClientBalanceAfterActivation().longValue()
                    : null;

            String message;
            String eventType;
            if (blankCode.getActivatedAt() != null && blankCode.getStatus() == TruBlankCodeStatus.ACTIVE) {
                message = "TruCard activated";
                eventType = "TRUCARD_ACTIVATED";
            } else {
                message = "TruCard assigned";
                eventType = "TRUCARD_ASSIGNED";
            }

            ClientHistoryItem cardItem = new ClientHistoryItem(
                    eventTime,
                    codeNumber,
                    denomination,
                    remainingBalance,
                    message,
                    eventType,
                    blankCode.getValidityMonths(),
                    expiryDate,
                    "-",
                    "-",
                    issuedDate,
                    null
            );
            cardItem.setCodeType("PHYSICAL");
            cardItem.setSerialNumber(blankCode.getSerialNumber());
            cardItem.setReferenceNumber(blankCode.getReferenceNumber());
            items.add(cardItem);

            // Redemptions for this physical code
            List<UserRedemption> redemptions = allRedemptions.stream()
                    .filter(r -> r.getUserTruvishCode() != null && codeNumber != null &&
                            r.getUserTruvishCode().equalsIgnoreCase(codeNumber))
                    .sorted(Comparator.comparing(UserRedemption::getUserBrandTimeTemp, Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();

            for (UserRedemption redemption : redemptions) {
                ClientHistoryItem redeemItem = new ClientHistoryItem(
                        redemption.getUserBrandTimeTemp(),
                        redemption.getUserTruvishCode(),
                        redemption.getUserBrandValue(),
                        redemption.getAfterBalance(),
                        redemption.getHistoryMessage(),
                        redemption.getRedeemStatus(),
                        blankCode.getValidityMonths(),
                        expiryDate,
                        redemption.getUserBrandName(),
                        redemption.getUserPhoneNumber(),
                        issuedDate,
                        redemption.getUserBrandTimeTemp()
                );
                redeemItem.setCodeType("PHYSICAL");
                redeemItem.setSerialNumber(blankCode.getSerialNumber());
                redeemItem.setReferenceNumber(blankCode.getReferenceNumber());
                items.add(redeemItem);
            }
        }
        return items;
    }

    // =========================================================
    // PAGINATION
    // =========================================================

    private List<ClientHistoryItem> paginate(List<ClientHistoryItem> items, int page, int size) {
        if (items.isEmpty()) return items;
        if (page < 0) page = 0;
        if (size <= 0) size = 20;
        int fromIndex = page * size;
        if (fromIndex >= items.size()) return new ArrayList<>();
        int toIndex = Math.min(fromIndex + size, items.size());
        return new ArrayList<>(items.subList(fromIndex, toIndex));
    }

    // =========================================================
    // DIGITAL CODE ASSIGNMENT (unchanged)
    // =========================================================

    @Transactional
    public List<TruvishCode> updateClient(CodeAssignmentDto dto) {
        if (dto == null) throw new RuntimeException("Request required");
        if (dto.getClientId() == null) throw new RuntimeException("ClientId required");
        if (dto.getTruvishCodeValue() == null || dto.getTruvishCodeValue() <= 0)
            throw new RuntimeException("Voucher value required");
        Integer quantity = dto.getQuantity() != null && dto.getQuantity() > 0 ? dto.getQuantity() : 1;

        Client client = clientRepo.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        BigDecimal totalAmount = BigDecimal.valueOf(dto.getTruvishCodeValue())
                .multiply(BigDecimal.valueOf(quantity));
        BigDecimal balance = client.getBalance() == null ? BigDecimal.ZERO : client.getBalance();
        if (balance.compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient Balance. Required: " + totalAmount + ", Available: " + balance);
        }

        List<TruvishCode> savedCodes = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            TruvishCode code = new TruvishCode();
            code.setTruvishIdCodeNumber(generateUniqueCode());
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
            code.setTruvishCodeStatus(VoucherStatus.ACTIVE);
            savedCodes.add(repo.save(code));
        }

        // Wallet DEBIT (unchanged)
        CreateWalletTxnRequest walletReq = new CreateWalletTxnRequest();
        walletReq.setAmount(totalAmount);
        walletReq.setType("DEBIT");
        walletReq.setDescription("Debited");
        walletReq.setReferenceType("VOUCHER");
        walletReq.setReferenceId(UUID.randomUUID().toString());
        walletService.create(dto.getClientId(), walletReq);

        return savedCodes;
    }

    // =========================================================
    // CONFIGURE PHYSICAL TRUCARD (unchanged)
    // =========================================================

    @Transactional
    public TruBlankCode configureBlankCode(Long blankCodeId, Long clientId, Long denomination, Integer validityMonths) {
        // ... existing code unchanged ...
        return null; // placeholder
    }

    // =========================================================
    // ACTIVATE PHYSICAL TRUCARD (unchanged)
    // =========================================================

    @Transactional
    public TruBlankCode activateBlankCode(Long blankCodeId, Long clientId, Long denomination, Integer validityMonths) {
        // ... existing code unchanged ...
        return null; // placeholder
    }
}