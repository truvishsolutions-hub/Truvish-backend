package com.truvish.truvishbackend.TruBlankCode;

import com.truvish.truvishbackend.TruBlankCode.dto.ActivateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.AssignClientRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.GenerateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.UpdateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.response.TruBlankCodeResponse;
import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import com.truvish.truvishbackend.redemption.UserRedemption;
import com.truvish.truvishbackend.redemption.UserRedemptionRepository;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TruBlankCodeService {

    private final TruBlankCodeRepository repository;
    private final ClientRepository clientRepository;
    private final WalletTransactionService walletTransactionService;
    private final UserRedemptionRepository userRedemptionRepository;  // new

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long SERIAL_BASE = 324243145000L;

    // =========================================================
    // CONSTRUCTOR (updated)
    // =========================================================

    public TruBlankCodeService(
            TruBlankCodeRepository repository,
            ClientRepository clientRepository,
            WalletTransactionService walletTransactionService,
            UserRedemptionRepository userRedemptionRepository
    ) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.walletTransactionService = walletTransactionService;
        this.userRedemptionRepository = userRedemptionRepository;
    }

    // =========================================================
    // GENERATE BLANK CODES
    // =========================================================

    public List<TruBlankCodeResponse> generateCodes(
            GenerateBlankCodeRequest request,
            Long adminId
    ) {
        // ... (unchanged, same as before) ...
        if (request == null) {
            throw new RuntimeException("Generate request cannot be null");
        }
        if (request.getQuantity() == null) {
            throw new RuntimeException("Quantity is required");
        }
        if (request.getQuantity() < 1) {
            throw new RuntimeException("Quantity must be at least 1");
        }
        if (adminId == null) {
            throw new RuntimeException("adminId is required");
        }

        int quantity = request.getQuantity();

        Long generationNumber = repository
                .findTopByOrderByGenerationNumberDesc()
                .map(code -> {
                    Long last = code.getGenerationNumber();
                    return (last == null) ? 1L : last + 1;
                })
                .orElse(1L);

        long nextSerial = getNextSerialNumber();

        LocalDateTime now = LocalDateTime.now();
        List<TruBlankCode> generatedCodes = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            TruBlankCode code = new TruBlankCode();
            code.setGenerationNumber(generationNumber);
            code.setSerialNumber(String.valueOf(nextSerial + i));
            code.setCodeNumber(generateCodeNumber());
            code.setReferenceNumber(generateReferenceNumber());

            code.setStatus(TruBlankCodeStatus.INACTIVE);
            code.setDenomination(null);
            code.setValidityMonths(null);
            code.setExpiryDate(null);
            code.setBrandNames(new ArrayList<>());
            code.setBrandCategory(null);
            code.setThemeName(null);
            code.setThemeImg(null);
            code.setClientId(null);
            code.setClientName(null);
            code.setClientImg(null);
            code.setClientBrand(new ArrayList<>());
            code.setClientCategory(null);
            code.setClientTheme(null);
            code.setClientThemeImg(null);
            code.setClientBalanceBeforeActivation(null);
            code.setClientBalanceAfterActivation(null);

            code.setCreatedBy(adminId);
            code.setCreatedAt(now);
            code.setUpdatedAt(now);
            code.setActivatedAt(null);
            code.setActivatedBy(null);
            code.setRedeemedAt(null);

            generatedCodes.add(code);
        }

        List<TruBlankCode> savedCodes = repository.saveAll(generatedCodes);
        return savedCodes.stream().map(this::toResponse).toList();
    }

    // =========================================================
    // ASSIGN CLIENT (unchanged)
    // =========================================================

    public TruBlankCodeResponse assignClient(Long id, AssignClientRequest request) {
        // ... same as before ...
        validateId(id);
        if (request == null) throw new RuntimeException("Assign client request cannot be null");
        if (request.getClientId() == null) throw new RuntimeException("clientId is required");

        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));

        if (code.getStatus() != TruBlankCodeStatus.INACTIVE) {
            throw new RuntimeException("Only INACTIVE code can be assigned");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found: " + request.getClientId()));

        code.setClientId(client.getId());
        code.setClientName(cleanString(client.getClientName()));
        code.setClientImg(cleanString(client.getLogoImg()));

        List<String> clientBrands = request.getClientBrand();
        if (clientBrands == null) clientBrands = new ArrayList<>();
        code.setClientBrand(new ArrayList<>(clientBrands));
        code.setClientCategory(cleanString(request.getClientCategory()));
        code.setClientTheme(cleanString(request.getClientTheme()));
        code.setClientThemeImg(cleanString(request.getClientThemeImg()));
        code.setUpdatedAt(LocalDateTime.now());

        return toResponse(repository.save(code));
    }

    // =========================================================
    // UPDATE BLANK CODE (unchanged)
    // =========================================================

    public TruBlankCodeResponse updateCode(Long id, UpdateBlankCodeRequest request) {
        validateId(id);
        if (request == null) throw new RuntimeException("Update request cannot be null");

        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));

        if (code.getStatus() != TruBlankCodeStatus.INACTIVE) {
            throw new RuntimeException("Only INACTIVE code can be updated");
        }

        if (request.getDenomination() != null) {
            Double value = request.getDenomination();
            if (value <= 0) throw new RuntimeException("Denomination must be greater than 0");
            if (value % 1 != 0) throw new RuntimeException("Denomination must be a whole number");
            code.setDenomination(value.longValue());
        }

        if (request.getValidityMonths() != null) {
            if (request.getValidityMonths() < 1) throw new RuntimeException("Validity months must be greater than 0");
            code.setValidityMonths(request.getValidityMonths());
        }

        if (request.getBrandNames() != null) {
            code.setBrandNames(new ArrayList<>(request.getBrandNames()));
        }
        if (request.getBrandCategory() != null) {
            code.setBrandCategory(cleanString(request.getBrandCategory()));
        }
        if (request.getThemeName() != null) {
            code.setThemeName(cleanString(request.getThemeName()));
        }
        if (request.getThemeImg() != null) {
            code.setThemeImg(cleanString(request.getThemeImg()));
        }

        code.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(code));
    }

    // =========================================================
    // ACTIVATE CODE (unchanged)
    // =========================================================

    public TruBlankCodeResponse activateCode(Long id, ActivateBlankCodeRequest request) {
        validateId(id);
        if (request == null) throw new RuntimeException("Activation request cannot be null");
        if (request.getAdminId() == null) throw new RuntimeException("adminId is required");
        if (request.getClientId() == null) throw new RuntimeException("clientId is required");

        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));

        if (code.getStatus() != TruBlankCodeStatus.INACTIVE) {
            throw new RuntimeException("Only INACTIVE code can be activated");
        }
        if (code.getClientId() == null) {
            throw new RuntimeException("Client must be assigned before activation");
        }
        if (!code.getClientId().equals(request.getClientId())) {
            throw new RuntimeException("Activation client does not match assigned client");
        }

        if (code.getDenomination() == null || code.getDenomination() < 1) {
            throw new RuntimeException("Denomination must be set and > 0 before activation");
        }
        if (code.getValidityMonths() == null || code.getValidityMonths() < 1) {
            throw new RuntimeException("Validity months must be set and > 0 before activation");
        }

        Client client = clientRepository.findById(code.getClientId())
                .orElseThrow(() -> new RuntimeException("Assigned client not found: " + code.getClientId()));

        BigDecimal balanceBefore = client.getBalance() != null ? client.getBalance() : BigDecimal.ZERO;
        balanceBefore = balanceBefore.setScale(2, RoundingMode.HALF_UP);

        BigDecimal denomination = BigDecimal.valueOf(code.getDenomination()).setScale(2, RoundingMode.HALF_UP);

        if (balanceBefore.compareTo(denomination) < 0) {
            throw new RuntimeException("Insufficient client wallet balance. Available: " + balanceBefore + ", Required: " + denomination);
        }

        int updatedRows = clientRepository.deductBalance(client.getId(), denomination);
        if (updatedRows != 1) {
            throw new RuntimeException("Unable to deduct client wallet balance. Client balance may have changed.");
        }

        walletTransactionService.recordDebit(
                client.getId(),
                denomination,
                "TruBlankCode activation - " + code.getCodeNumber(),
                "TRU_BLANK_CODE",
                String.valueOf(code.getId())
        );

        BigDecimal balanceAfter = balanceBefore.subtract(denomination).setScale(2, RoundingMode.HALF_UP);
        LocalDateTime activatedAt = LocalDateTime.now();

        // Update client snapshot
        code.setClientId(client.getId());
        code.setClientName(cleanString(client.getClientName()));
        code.setClientImg(cleanString(client.getLogoImg()));

        List<String> clientBrands = request.getClientBrand();
        if (clientBrands == null) {
            clientBrands = code.getClientBrand() != null ? code.getClientBrand() : new ArrayList<>();
        }
        code.setClientBrand(new ArrayList<>(clientBrands));

        if (request.getClientCategory() != null) {
            code.setClientCategory(cleanString(request.getClientCategory()));
        }
        if (request.getClientTheme() != null) {
            code.setClientTheme(cleanString(request.getClientTheme()));
        }
        if (request.getClientThemeImg() != null) {
            code.setClientThemeImg(cleanString(request.getClientThemeImg()));
        }

        code.setClientBalanceBeforeActivation(balanceBefore);
        code.setClientBalanceAfterActivation(balanceAfter);
        code.setActivatedAt(activatedAt);
        code.setActivatedBy(request.getAdminId());
        code.setExpiryDate(activatedAt.plusMonths(code.getValidityMonths()));
        code.setStatus(TruBlankCodeStatus.ACTIVE);
        code.setUpdatedAt(activatedAt);

        return toResponse(repository.save(code));
    }

    // =========================================================
    // REDEEM CODE (unchanged)
    // =========================================================

    public TruBlankCodeResponse redeemCode(Long id) {
        validateId(id);

        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));

        if (code.getStatus() != TruBlankCodeStatus.ACTIVE) {
            throw new RuntimeException("Only ACTIVE code can be redeemed");
        }

        LocalDateTime now = LocalDateTime.now();

        if (isExpired(code, now)) {
            markExpired(code, now);
            throw new RuntimeException("Code has expired");
        }

        code.setStatus(TruBlankCodeStatus.REDEEMED);
        code.setRedeemedAt(now);
        code.setUpdatedAt(now);

        return toResponse(repository.save(code));
    }

    // =========================================================
    // DEACTIVATE CODE (unchanged)
    // =========================================================

    public TruBlankCodeResponse deactivateCode(Long id) {
        validateId(id);
        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));
        if (code.getStatus() != TruBlankCodeStatus.ACTIVE) {
            throw new RuntimeException("Only ACTIVE code can be deactivated");
        }
        throw new RuntimeException("Deactivation is disabled after activation because wallet amount has already been deducted");
    }

    // =========================================================
    // CANCEL CODE (unchanged)
    // =========================================================

    public TruBlankCodeResponse cancelCode(Long id) {
        validateId(id);
        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));

        if (code.getStatus() == TruBlankCodeStatus.CANCELLED) {
            throw new RuntimeException("Blank code is already cancelled");
        }
        if (code.getStatus() == TruBlankCodeStatus.REDEEMED) {
            throw new RuntimeException("Redeemed code cannot be cancelled");
        }

        code.setStatus(TruBlankCodeStatus.CANCELLED);
        code.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(code));
    }

    // =========================================================
    // GET ALL, GET BY CLIENT, etc. (unchanged)
    // =========================================================

    @Transactional(readOnly = true)
    public Page<TruBlankCodeResponse> getAllCodes(Pageable pageable) {
        refreshExpiredCodes();
        return repository.findAllByOrderByCreatedAtDesc(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<TruBlankCodeResponse> getCodesByClient(Long clientId) {
        validateId(clientId);
        refreshExpiredCodes();
        return repository.findByClientIdOrderByCreatedAtDesc(clientId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Page<TruBlankCodeResponse> getCodesByClient(Long clientId, Pageable pageable) {
        validateId(clientId);
        refreshExpiredCodes();
        return repository.findByClientIdOrderByCreatedAtDesc(clientId, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public TruBlankCodeResponse getById(Long id) {
        validateId(id);
        TruBlankCode code = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blank code not found: " + id));
        refreshExpiredCode(code);
        return toResponse(code);
    }

    @Transactional(readOnly = true)
    public TruBlankCodeResponse getBySerial(String serialNumber) {
        validateSearchValue(serialNumber, "Serial number");
        String value = serialNumber.trim();
        TruBlankCode code = repository.findBySerialNumber(value)
                .orElseThrow(() -> new RuntimeException("Blank code not found for serial: " + value));
        refreshExpiredCode(code);
        return toResponse(code);
    }

    @Transactional(readOnly = true)
    public TruBlankCodeResponse getByReference(String referenceNumber) {
        validateSearchValue(referenceNumber, "Reference number");
        String value = referenceNumber.trim();
        TruBlankCode code = repository.findByReferenceNumber(value)
                .orElseThrow(() -> new RuntimeException("Blank code not found for reference: " + value));
        refreshExpiredCode(code);
        return toResponse(code);
    }

    @Transactional(readOnly = true)
    public TruBlankCodeResponse getByCodeNumber(String codeNumber) {
        validateSearchValue(codeNumber, "Code number");
        String value = codeNumber.trim();
        TruBlankCode code = repository.findByCodeNumber(value)
                .orElseThrow(() -> new RuntimeException("Blank code not found for code: " + value));
        refreshExpiredCode(code);
        return toResponse(code);
    }

    @Transactional(readOnly = true)
    public TruBlankCodeResponse search(String value) {
        validateSearchValue(value, "Search value");
        String searchValue = value.trim();

        var byCode = repository.findByCodeNumber(searchValue);
        if (byCode.isPresent()) {
            TruBlankCode code = byCode.get();
            refreshExpiredCode(code);
            return toResponse(code);
        }

        var bySerial = repository.findBySerialNumber(searchValue);
        if (bySerial.isPresent()) {
            TruBlankCode code = bySerial.get();
            refreshExpiredCode(code);
            return toResponse(code);
        }

        var byReference = repository.findByReferenceNumber(searchValue);
        if (byReference.isPresent()) {
            TruBlankCode code = byReference.get();
            refreshExpiredCode(code);
            return toResponse(code);
        }

        throw new RuntimeException("Blank code not found for: " + searchValue);
    }

    @Transactional(readOnly = true)
    public Page<TruBlankCodeResponse> getByStatus(TruBlankCodeStatus status, Pageable pageable) {
        if (status == null) throw new RuntimeException("Status is required");
        refreshExpiredCodes();
        return repository.findByStatusOrderByCreatedAtDesc(status, pageable).map(this::toResponse);
    }

    // =========================================================
    // COUNTS (unchanged)
    // =========================================================

    @Transactional(readOnly = true)
    public long countInactive() { return repository.countByStatus(TruBlankCodeStatus.INACTIVE); }

    @Transactional(readOnly = true)
    public long countActive() {
        refreshExpiredCodes();
        return repository.countByStatus(TruBlankCodeStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public long countRedeemed() { return repository.countByStatus(TruBlankCodeStatus.REDEEMED); }

    @Transactional(readOnly = true)
    public long countExpired() {
        refreshExpiredCodes();
        return repository.countByStatus(TruBlankCodeStatus.EXPIRED);
    }

    @Transactional(readOnly = true)
    public long countCancelled() { return repository.countByStatus(TruBlankCodeStatus.CANCELLED); }

    // =========================================================
    // CLIENT COUNTS (unchanged)
    // =========================================================

    @Transactional(readOnly = true)
    public long countClientCodes(Long clientId) {
        validateId(clientId);
        return repository.countByClientId(clientId);
    }

    @Transactional(readOnly = true)
    public long countClientInactive(Long clientId) {
        validateId(clientId);
        return repository.countByClientIdAndStatus(clientId, TruBlankCodeStatus.INACTIVE);
    }

    @Transactional(readOnly = true)
    public long countClientActive(Long clientId) {
        validateId(clientId);
        refreshExpiredCodes();
        return repository.countClientActiveCodes(clientId, TruBlankCodeStatus.ACTIVE, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public long countClientRedeemed(Long clientId) {
        validateId(clientId);
        return repository.countByClientIdAndStatus(clientId, TruBlankCodeStatus.REDEEMED);
    }

    @Transactional(readOnly = true)
    public long countClientExpired(Long clientId) {
        validateId(clientId);
        refreshExpiredCodes();
        return repository.countClientExpiredCodes(clientId, TruBlankCodeStatus.EXPIRED, TruBlankCodeStatus.ACTIVE, LocalDateTime.now());
    }

    // =========================================================
    // CLIENT VALUES (unchanged)
    // =========================================================

    @Transactional(readOnly = true)
    public BigDecimal clientIssuedValue(Long clientId) {
        validateId(clientId);
        return safeAmount(repository.sumDenominationByClientId(clientId));
    }

    @Transactional(readOnly = true)
    public BigDecimal clientInactiveValue(Long clientId) {
        validateId(clientId);
        return safeAmount(repository.sumDenominationByClientIdAndStatus(clientId, TruBlankCodeStatus.INACTIVE));
    }

    @Transactional(readOnly = true)
    public BigDecimal clientActiveValue(Long clientId) {
        validateId(clientId);
        refreshExpiredCodes();
        return safeAmount(repository.sumClientActiveValue(clientId, TruBlankCodeStatus.ACTIVE, LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public BigDecimal clientRedeemedValue(Long clientId) {
        validateId(clientId);
        return safeAmount(repository.sumDenominationByClientIdAndStatus(clientId, TruBlankCodeStatus.REDEEMED));
    }

    @Transactional(readOnly = true)
    public BigDecimal clientExpiredValue(Long clientId) {
        validateId(clientId);
        refreshExpiredCodes();
        return safeAmount(repository.sumClientExpiredValue(clientId, TruBlankCodeStatus.EXPIRED, TruBlankCodeStatus.ACTIVE, LocalDateTime.now()));
    }

    // =========================================================
    // REFRESH EXPIRED CODES (unchanged)
    // =========================================================

    @Transactional
    public int refreshExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();
        List<TruBlankCode> expiredCodes = repository.findExpiredActiveCodes(TruBlankCodeStatus.ACTIVE, now);
        if (expiredCodes.isEmpty()) return 0;
        for (TruBlankCode code : expiredCodes) {
            code.setStatus(TruBlankCodeStatus.EXPIRED);
            code.setUpdatedAt(now);
        }
        repository.saveAll(expiredCodes);
        return expiredCodes.size();
    }

    private void refreshExpiredCode(TruBlankCode code) {
        if (code == null) return;
        LocalDateTime now = LocalDateTime.now();
        if (isExpired(code, now)) {
            markExpired(code, now);
        }
    }

    private boolean isExpired(TruBlankCode code, LocalDateTime now) {
        return code.getExpiryDate() != null && !now.isBefore(code.getExpiryDate());
    }

    private void markExpired(TruBlankCode code, LocalDateTime now) {
        code.setStatus(TruBlankCodeStatus.EXPIRED);
        code.setUpdatedAt(now);
        repository.save(code);
    }

    // =========================================================
    // UTILITY METHODS (unchanged)
    // =========================================================

    private long getNextSerialNumber() {
        return repository.findTopByOrderBySerialNumberDesc()
                .map(code -> {
                    String serial = code.getSerialNumber();
                    if (serial == null || serial.trim().isEmpty()) return SERIAL_BASE + 1;
                    try {
                        return Long.parseLong(serial.trim()) + 1;
                    } catch (NumberFormatException e) {
                        return SERIAL_BASE + 1;
                    }
                })
                .orElse(SERIAL_BASE + 1);
    }

    private String generateCodeNumber() {
        String code;
        do {
            code = randomBlock(4) + "-" + randomBlock(4) + "-" + randomBlock(4);
        } while (repository.existsByCodeNumber(code));
        return code;
    }

    private String randomBlock(int length) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return builder.toString();
    }

    private String generateReferenceNumber() {
        String reference;
        do {
            String date = LocalDateTime.now().toLocalDate().toString().replace("-", "");
            reference = "REF-" + date + "-" + randomBlock(6);
        } while (repository.existsByReferenceNumber(reference));
        return reference;
    }

    private void validateId(Long id) {
        if (id == null) throw new RuntimeException("Blank code ID is required");
        if (id < 1) throw new RuntimeException("Blank code ID must be greater than 0");
    }

    private void validateSearchValue(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException(fieldName + " is required");
        }
    }

    private String cleanString(String value) {
        return (value == null || value.trim().isEmpty()) ? null : value.trim();
    }

    private BigDecimal safeAmount(BigDecimal value) {
        return (value == null) ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : value.setScale(2, RoundingMode.HALF_UP);
    }

    // =========================================================
    // RESPONSE MAPPER (UPDATED with redeemedBy)
    // =========================================================

    private TruBlankCodeResponse toResponse(TruBlankCode code) {
        TruBlankCodeResponse response = new TruBlankCodeResponse();

        // BASIC
        response.setId(code.getId());
        response.setGenerationNumber(code.getGenerationNumber());
        response.setCodeNumber(code.getCodeNumber());
        response.setSerialNumber(code.getSerialNumber());
        response.setReferenceNumber(code.getReferenceNumber());

        // REWARD
        response.setDenomination(code.getDenomination());
        response.setValidityMonths(code.getValidityMonths());
        response.setExpiryDate(code.getExpiryDate());

        // TRUCARD BRAND
        response.setBrandNames(code.getBrandNames() != null ? new ArrayList<>(code.getBrandNames()) : new ArrayList<>());
        response.setBrandCategory(code.getBrandCategory());
        response.setThemeName(code.getThemeName());
        response.setThemeImg(code.getThemeImg());

        // STATUS
        response.setStatus(code.getStatus());

        // LIFECYCLE
        response.setCreatedAt(code.getCreatedAt());
        response.setUpdatedAt(code.getUpdatedAt());
        response.setActivatedAt(code.getActivatedAt());
        response.setRedeemedAt(code.getRedeemedAt());
        response.setCreatedBy(code.getCreatedBy());
        response.setActivatedBy(code.getActivatedBy());

        // CLIENT
        response.setClientId(code.getClientId());
        response.setClientName(code.getClientName());
        response.setClientImg(code.getClientImg());
        response.setClientBrand(code.getClientBrand() != null ? new ArrayList<>(code.getClientBrand()) : new ArrayList<>());
        response.setClientCategory(code.getClientCategory());
        response.setClientTheme(code.getClientTheme());
        response.setClientThemeImg(code.getClientThemeImg());

        // BALANCE SNAPSHOT
        response.setClientBalanceBeforeActivation(code.getClientBalanceBeforeActivation());
        response.setClientBalanceAfterActivation(code.getClientBalanceAfterActivation());

        // =====================================================
        // REDEEMED BY – only if status is REDEEMED
        // =====================================================
        String redeemedBy = null;
        if (code.getStatus() == TruBlankCodeStatus.REDEEMED) {
            List<UserRedemption> redemptions = userRedemptionRepository
                    .findByUserTruvishCodeOrderByUserBrandTimeTempDesc(code.getCodeNumber());
            if (!redemptions.isEmpty()) {
                redeemedBy = redemptions.get(0).getUserPhoneNumber();
            }
        }
        response.setRedeemedBy(redeemedBy);

        return response;
    }
}