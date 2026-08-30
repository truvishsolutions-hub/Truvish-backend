package com.truvish.truvishbackend.TruCard.service;

import com.truvish.truvishbackend.TruCard.dto.TruCardCodeRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardCodeResponse;
import com.truvish.truvishbackend.TruCard.entity.TruCardCode;
import com.truvish.truvishbackend.TruCard.enums.TruCardCodeStatus;
import com.truvish.truvishbackend.TruCard.repository.TruCardCodeRepository;
import com.truvish.truvishbackend.admin.AdminConfig;
import com.truvish.truvishbackend.admin.AdminConfigService;
import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TruCardCodeService {

    private final TruCardCodeRepository codeRepository;
    private final ClientRepository clientRepository;
    private final WalletTransactionService walletTransactionService;
    private final AdminConfigService adminConfigService;

    public TruCardCodeService(
            TruCardCodeRepository codeRepository,
            ClientRepository clientRepository,
            WalletTransactionService walletTransactionService,
            AdminConfigService adminConfigService
    ) {
        this.codeRepository = codeRepository;
        this.clientRepository = clientRepository;
        this.walletTransactionService = walletTransactionService;
        this.adminConfigService = adminConfigService;
    }

    // =========================================================
    // GET ALL
    // =========================================================

    public List<TruCardCodeResponse> getAllCodes() {

        return codeRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET CLIENT CODES
    // =========================================================

    public List<TruCardCodeResponse> getClientCodes(Long clientId) {

        return codeRepository
                .findByClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET CODES BY CLIENT
    // =========================================================

    public List<TruCardCodeResponse> getCodesByClient(Long clientId) {

        return getClientCodes(clientId);
    }

    // =========================================================
    // GET ORDER CODES
    // =========================================================

    public List<TruCardCodeResponse> getOrderCodes(Long orderId) {

        return codeRepository
                .findByOrderId(orderId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET CAMPAIGN CODES
    // =========================================================

    public List<TruCardCodeResponse> getCampaignCodes(Long campaignId) {

        return codeRepository
                .findByCampaignId(campaignId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET CLIENT CODES BY STATUS
    // =========================================================

    public List<TruCardCodeResponse> getClientCodesByStatus(
            Long clientId,
            TruCardCodeStatus status
    ) {

        return codeRepository
                .findByClientIdAndStatusOrderByCreatedAtDesc(
                        clientId,
                        status
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET ALL BY STATUS
    // =========================================================

    public List<TruCardCodeResponse> getAllCodesByStatus(
            TruCardCodeStatus status
    ) {

        return codeRepository
                .findByStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GET BY ID
    // =========================================================

    public TruCardCodeResponse getCodeById(Long id) {

        return mapToResponse(
                findCodeEntity(id)
        );
    }

    // =========================================================
    // GET BY CODE NUMBER
    // =========================================================

    public TruCardCodeResponse getByCodeNumber(String codeNumber) {

        if (codeNumber == null || codeNumber.isBlank()) {

            throw new RuntimeException(
                    "Redeem code is required"
            );
        }

        TruCardCode code =
                codeRepository
                        .findByCodeNumber(
                                codeNumber
                                        .trim()
                                        .toUpperCase()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard code not found: "
                                                + codeNumber
                                )
                        );

        return mapToResponse(code);
    }

    // =========================================================
    // GET BY SERIAL NUMBER
    // =========================================================

    public TruCardCodeResponse getBySerialNumber(
            String serialNumber
    ) {

        if (serialNumber == null ||
                serialNumber.isBlank()) {

            throw new RuntimeException(
                    "Serial number is required"
            );
        }

        TruCardCode code =
                codeRepository
                        .findBySerialNumber(
                                serialNumber.trim()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard not found with serial number: "
                                                + serialNumber
                                )
                        );

        return mapToResponse(code);
    }

    // =========================================================
    // GET BY REFERENCE NUMBER
    // =========================================================

    public TruCardCodeResponse getByReferenceNumber(
            String referenceNumber
    ) {

        if (referenceNumber == null ||
                referenceNumber.isBlank()) {

            throw new RuntimeException(
                    "Reference number is required"
            );
        }

        TruCardCode code =
                codeRepository
                        .findByReferenceNumber(
                                referenceNumber
                                        .trim()
                                        .toUpperCase()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard not found with reference number: "
                                                + referenceNumber
                                )
                        );

        return mapToResponse(code);
    }

    // =========================================================
    // SEARCH
    // =========================================================

    public List<TruCardCodeResponse> searchCodes(
            String keyword
    ) {

        if (keyword == null ||
                keyword.isBlank()) {

            return getAllCodes();
        }

        String value =
                keyword.trim();

        return codeRepository
                .findByCodeNumberContainingIgnoreCaseOrSerialNumberContainingOrReferenceNumberContainingIgnoreCase(
                        value,
                        value,
                        value
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // =========================================================
    // GENERATE UNIQUE CODE
    // =========================================================

    public String generateUniqueCodeNumber() {

        String codeNumber;

        do {

            codeNumber =
                    generateRedeemCode();

        } while (
                codeRepository.existsByCodeNumber(
                        codeNumber
                )
        );

        return codeNumber;
    }

    // =========================================================
    // REDEEM CODE GENERATOR
    // =========================================================

    private String generateRedeemCode() {

        String uuid =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .toUpperCase();

        return uuid.substring(0, 4)
                + "-"
                + uuid.substring(4, 8)
                + "-"
                + uuid.substring(8, 12);
    }

    // =========================================================
    // GENERATE UNIQUE REFERENCE
    // =========================================================

    public String generateUniqueReferenceNumber() {

        String referenceNumber;

        do {

            String uuid =
                    UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .toUpperCase();

            referenceNumber =
                    "TC-" + uuid.substring(0, 8);

        } while (
                codeRepository.existsByReferenceNumber(
                        referenceNumber
                )
        );

        return referenceNumber;
    }

    // =========================================================
    // SERIAL NUMBER
    // =========================================================

    @Transactional
    public synchronized String generateNextSerialNumber() {

        final long prefix = 583214739L;

        TruCardCode latestCode =
                codeRepository
                        .findTopByOrderBySerialNumberDesc()
                        .orElse(null);

        if (latestCode == null ||
                latestCode.getSerialNumber() == null ||
                latestCode.getSerialNumber().isBlank()) {

            return prefix + "001";
        }

        String latestSerial =
                latestCode.getSerialNumber();

        if (latestSerial.length() < 3) {

            throw new RuntimeException(
                    "Invalid TruCard serial number: "
                            + latestSerial
            );
        }

        String sequencePart =
                latestSerial.substring(
                        latestSerial.length() - 3
                );

        long latestSequence;

        try {

            latestSequence =
                    Long.parseLong(sequencePart);

        } catch (NumberFormatException exception) {

            throw new RuntimeException(
                    "Invalid TruCard serial sequence: "
                            + latestSerial
            );
        }

        long nextSequence =
                latestSequence + 1;

        // Skip 009, 019, 029...
        if (nextSequence % 10 == 9) {
            nextSequence++;
        }

        String nextSerial =
                prefix +
                        String.format(
                                "%03d",
                                nextSequence
                        );

        while (
                codeRepository.existsBySerialNumber(
                        nextSerial
                )
        ) {

            nextSequence++;

            if (nextSequence % 10 == 9) {
                nextSequence++;
            }

            nextSerial =
                    prefix +
                            String.format(
                                    "%03d",
                                    nextSequence
                            );
        }

        return nextSerial;
    }

    // =========================================================
    // CREATE SINGLE CODE
    // =========================================================

    private TruCardCode createCode(
            TruCardCodeRequest request,
            String verifiedThemeName,
            String verifiedThemeImg
    ) {

        TruCardCode code =
                new TruCardCode();

        code.setClientId(
                request.getClientId()
        );

        // Direct generation = no order/campaign
        code.setOrderId(null);
        code.setCampaignId(null);

        code.setSerialNumber(
                generateNextSerialNumber()
        );

        code.setReferenceNumber(
                generateUniqueReferenceNumber()
        );

        code.setCodeNumber(
                generateUniqueCodeNumber()
        );

        code.setDenomination(
                request.getDenomination()
        );

        // New card starts inactive
        code.setStatus(
                TruCardCodeStatus.INACTIVE
        );

        // Validity will be added before activation
        code.setValidityMonths(null);

        // IMPORTANT:
        // Save verified admin theme values,
        // not arbitrary request image.
        code.setThemeName(
                verifiedThemeName
        );

        code.setThemeImg(
                verifiedThemeImg
        );

        code.setActivatedAt(null);
        code.setExpiryDate(null);
        code.setRedeemedAt(null);

        return codeRepository.save(code);
    }

    // =========================================================
    // GENERATE MULTIPLE CODES
    // =========================================================

    @Transactional
    public List<TruCardCodeResponse> generateCodes(
            TruCardCodeRequest request
    ) {

        validateCodeRequest(request);

        Client client =
                clientRepository
                        .findById(
                                request.getClientId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Client not found: "
                                                + request.getClientId()
                                )
                        );

        // =====================================================
        // FIND + VERIFY THEME
        // =====================================================

        ThemeData selectedTheme =
                findAndValidateTheme(
                        request.getThemeName(),
                        request.getThemeImg()
                );

        int quantity =
                request.getQuantity();

        BigDecimal totalAmount =
                request.getDenomination()
                        .multiply(
                                BigDecimal.valueOf(quantity)
                        );

        BigDecimal currentBalance =
                client.getBalance() == null
                        ? BigDecimal.ZERO
                        : client.getBalance();

        if (currentBalance.compareTo(totalAmount) < 0) {

            throw new RuntimeException(
                    "Insufficient wallet balance. "
                            + "Required: ₹"
                            + totalAmount
                            + ", Available: ₹"
                            + currentBalance
            );
        }

        // =====================================================
        // ONE WALLET DEBIT
        // =====================================================

        CreateWalletTxnRequest walletRequest =
                new CreateWalletTxnRequest();

        walletRequest.setType("DEBIT");

        walletRequest.setAmount(
                totalAmount
        );

        walletRequest.setDescription(
                "TruCard generation - "
                        + quantity
                        + " card(s) × ₹"
                        + request.getDenomination()
        );

        walletRequest.setReferenceType(
                "TRUCARD_GENERATION"
        );

        walletRequest.setReferenceId(
                "CLIENT-" + request.getClientId()
        );

        walletTransactionService.create(
                request.getClientId(),
                walletRequest
        );

        // =====================================================
        // GENERATE CARDS
        // =====================================================

        List<TruCardCodeResponse> responses =
                new ArrayList<>();

        for (int i = 0; i < quantity; i++) {

            TruCardCode savedCode =
                    createCode(
                            request,
                            selectedTheme.name,
                            selectedTheme.image
                    );

            responses.add(
                    mapToResponse(savedCode)
            );
        }

        return responses;
    }

    // =========================================================
    // THEME DATA
    // =========================================================

    private static class ThemeData {

        private final String name;
        private final String image;

        private ThemeData(
                String name,
                String image
        ) {
            this.name = name;
            this.image = image;
        }
    }

    // =========================================================
    // FIND + VALIDATE THEME
    //
    // Theme name is the main identifier.
    //
    // Example:
    //
    // THANK YOU
    // /uploads/1782137013886_Thank You.jpeg
    //
    // If request sends:
    //
    // THANK YOU
    // http://localhost:8080/uploads/1782137013886_Thank You.jpeg
    //
    // it will still match.
    // =========================================================

    private ThemeData findAndValidateTheme(
            String themeName,
            String themeImg
    ) {

        if (themeName == null ||
                themeName.isBlank()) {

            throw new RuntimeException(
                    "Theme selection is required"
            );
        }

        if (themeImg == null ||
                themeImg.isBlank()) {

            throw new RuntimeException(
                    "Theme image is required"
            );
        }

        AdminConfig config =
                adminConfigService.getConfig();

        if (config == null) {

            throw new RuntimeException(
                    "Admin configuration not found"
            );
        }

        String selectedName =
                normalizeThemeName(themeName);

        String selectedImg =
                normalizePath(themeImg);

        // =====================================================
        // THEME 1
        // =====================================================

        ThemeData theme1 =
                checkTheme(
                        selectedName,
                        selectedImg,
                        config.getThemeName1(),
                        config.getThemeImg1()
                );

        if (theme1 != null) {
            return theme1;
        }

        // =====================================================
        // THEME 2
        // =====================================================

        ThemeData theme2 =
                checkTheme(
                        selectedName,
                        selectedImg,
                        config.getThemeName2(),
                        config.getThemeImg2()
                );

        if (theme2 != null) {
            return theme2;
        }

        // =====================================================
        // THEME 3
        // =====================================================

        ThemeData theme3 =
                checkTheme(
                        selectedName,
                        selectedImg,
                        config.getThemeName3(),
                        config.getThemeImg3()
                );

        if (theme3 != null) {
            return theme3;
        }

        // =====================================================
        // THEME 4
        // =====================================================

        ThemeData theme4 =
                checkTheme(
                        selectedName,
                        selectedImg,
                        config.getThemeName4(),
                        config.getThemeImg4()
                );

        if (theme4 != null) {
            return theme4;
        }

        // =====================================================
        // DEBUG
        // =====================================================

        throw new RuntimeException(
                "Selected theme is not available in admin configuration. "
                        + "Selected name: ["
                        + selectedName
                        + "], Selected image: ["
                        + selectedImg
                        + "]"
        );
    }

    // =========================================================
    // CHECK ONE THEME
    // =========================================================

    private ThemeData checkTheme(
            String selectedName,
            String selectedImg,
            String adminName,
            String adminImg
    ) {

        if (adminName == null ||
                adminName.isBlank()) {

            return null;
        }

        if (adminImg == null ||
                adminImg.isBlank()) {

            return null;
        }

        String normalizedAdminName =
                normalizeThemeName(adminName);

        String normalizedAdminImg =
                normalizePath(adminImg);

        // Theme name MUST match
        if (!normalizedAdminName.equals(
                selectedName
        )) {

            return null;
        }

        // =====================================================
        // IMAGE MATCH
        //
        // Exact normalized match first.
        // =====================================================

        if (normalizedAdminImg.equals(
                selectedImg
        )) {

            return new ThemeData(
                    adminName.trim(),
                    normalizedAdminImg
            );
        }

        // =====================================================
        // IMAGE FILENAME MATCH
        //
        // Handles:
        //
        // /uploads/file.jpeg
        // http://localhost:8080/uploads/file.jpeg
        // https://domain.com/uploads/file.jpeg
        // =====================================================

        String adminFileName =
                getFileName(normalizedAdminImg);

        String selectedFileName =
                getFileName(selectedImg);

        if (!adminFileName.isBlank() &&
                adminFileName.equalsIgnoreCase(
                        selectedFileName
                )) {

            return new ThemeData(
                    adminName.trim(),
                    normalizedAdminImg
            );
        }

        return null;
    }

    // =========================================================
    // NORMALIZE THEME NAME
    // =========================================================

    private String normalizeThemeName(
            String value
    ) {

        if (value == null) {
            return "";
        }

        return value
                .trim()
                .replaceAll("\\s+", " ")
                .toUpperCase();
    }

    // =========================================================
    // NORMALIZE PATH
    // =========================================================

    private String normalizePath(
            String value
    ) {

        if (value == null) {
            return "";
        }

        String path =
                value
                        .trim()
                        .replace("\\", "/");

        // Remove spaces around path
        path =
                path.trim();

        // Remove duplicate slash
        path =
                path.replaceAll(
                        "/+",
                        "/"
                );

        // Restore protocol after duplicate slash cleanup
        path =
                path.replace(
                        "http:/",
                        "http://"
                );

        path =
                path.replace(
                        "https:/",
                        "https://"
                );

        // If full URL contains /uploads/,
        // keep only /uploads/... part.
        int uploadsIndex =
                path.indexOf(
                        "/uploads/"
                );

        if (uploadsIndex >= 0) {

            path =
                    path.substring(
                            uploadsIndex
                    );
        }

        // uploads/file.jpeg -> /uploads/file.jpeg
        if (path.startsWith("uploads/")) {

            path =
                    "/" + path;
        }

        return path;
    }

    // =========================================================
    // GET FILE NAME
    // =========================================================

    private String getFileName(
            String path
    ) {

        if (path == null ||
                path.isBlank()) {

            return "";
        }

        String value =
                path.trim();

        int index =
                value.lastIndexOf("/");

        if (index >= 0 &&
                index < value.length() - 1) {

            return value.substring(
                    index + 1
            );
        }

        return value;
    }

    // =========================================================
    // UPDATE VALIDITY
    // =========================================================

    @Transactional
    public TruCardCodeResponse updateValidity(
            Long id,
            Integer validityMonths
    ) {

        if (validityMonths == null ||
                validityMonths <= 0) {

            throw new RuntimeException(
                    "Validity months must be greater than 0"
            );
        }

        TruCardCode code =
                findCodeEntity(id);

        if (code.getStatus() !=
                TruCardCodeStatus.INACTIVE) {

            throw new RuntimeException(
                    "Validity can only be changed while TruCard is inactive"
            );
        }

        code.setValidityMonths(
                validityMonths
        );

        code.setExpiryDate(null);

        return mapToResponse(
                codeRepository.save(code)
        );
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @Transactional
    public TruCardCodeResponse activateCode(
            Long id
    ) {

        TruCardCode code =
                findCodeEntity(id);

        if (code.getStatus() !=
                TruCardCodeStatus.INACTIVE) {

            throw new RuntimeException(
                    "Only inactive TruCard codes can be activated"
            );
        }

        if (code.getValidityMonths() == null ||
                code.getValidityMonths() <= 0) {

            throw new RuntimeException(
                    "Please set validity before activating the TruCard"
            );
        }

        LocalDateTime activatedAt =
                LocalDateTime.now();

        code.setActivatedAt(
                activatedAt
        );

        code.setExpiryDate(
                activatedAt.plusMonths(
                        code.getValidityMonths()
                )
        );

        code.setStatus(
                TruCardCodeStatus.ACTIVE
        );

        return mapToResponse(
                codeRepository.save(code)
        );
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Transactional
    public TruCardCodeResponse deactivateCode(
            Long id
    ) {

        TruCardCode code =
                findCodeEntity(id);

        if (code.getStatus() !=
                TruCardCodeStatus.ACTIVE) {

            throw new RuntimeException(
                    "Only active TruCard codes can be deactivated"
            );
        }

        code.setStatus(
                TruCardCodeStatus.INACTIVE
        );

        code.setActivatedAt(null);

        code.setExpiryDate(null);

        return mapToResponse(
                codeRepository.save(code)
        );
    }

    // =========================================================
    // REDEEM BY CODE NUMBER
    // =========================================================

    @Transactional
    public TruCardCodeResponse redeemCodeByCodeNumber(
            String codeNumber
    ) {

        if (codeNumber == null ||
                codeNumber.isBlank()) {

            throw new RuntimeException(
                    "Redeem code is required"
            );
        }

        TruCardCode code =
                codeRepository
                        .findByCodeNumber(
                                codeNumber
                                        .trim()
                                        .toUpperCase()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid TruCard code"
                                )
                        );

        return redeemCodeEntity(code);
    }

    // =========================================================
    // REDEEM BY ID
    // =========================================================

    @Transactional
    public TruCardCodeResponse redeemCode(
            Long id
    ) {

        TruCardCode code =
                findCodeEntity(id);

        return redeemCodeEntity(code);
    }

    // =========================================================
    // REDEEM ENTITY
    // =========================================================

    private TruCardCodeResponse redeemCodeEntity(
            TruCardCode code
    ) {

        if (code.getStatus() !=
                TruCardCodeStatus.ACTIVE) {

            throw new RuntimeException(
                    "Only active TruCard codes can be redeemed"
            );
        }

        if (code.getExpiryDate() != null &&
                LocalDateTime.now().isAfter(
                        code.getExpiryDate()
                )) {

            code.setStatus(
                    TruCardCodeStatus.EXPIRED_BACK_TO_WALLET
            );

            codeRepository.save(code);

            throw new RuntimeException(
                    "This TruCard has expired"
            );
        }

        code.setStatus(
                TruCardCodeStatus.REDEEMED
        );

        code.setRedeemedAt(
                LocalDateTime.now()
        );

        return mapToResponse(
                codeRepository.save(code)
        );
    }

    // =========================================================
    // EXPIRE
    // =========================================================

    @Transactional
    public TruCardCodeResponse expireCode(
            Long id
    ) {

        TruCardCode code =
                findCodeEntity(id);

        if (code.getStatus() ==
                TruCardCodeStatus.REDEEMED) {

            throw new RuntimeException(
                    "Redeemed TruCard cannot be expired"
            );
        }

        code.setStatus(
                TruCardCodeStatus.EXPIRED_BACK_TO_WALLET
        );

        return mapToResponse(
                codeRepository.save(code)
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Transactional
    public void deleteCode(
            Long id
    ) {

        TruCardCode code =
                findCodeEntity(id);

        codeRepository.delete(code);
    }

    // =========================================================
    // FIND ENTITY
    // =========================================================

    private TruCardCode findCodeEntity(
            Long id
    ) {

        return codeRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "TruCard code not found with id: "
                                        + id
                        )
                );
    }

    // =========================================================
    // VALIDATE REQUEST
    // =========================================================

    private void validateCodeRequest(
            TruCardCodeRequest request
    ) {

        if (request == null) {

            throw new RuntimeException(
                    "TruCard request is required"
            );
        }

        if (request.getClientId() == null) {

            throw new RuntimeException(
                    "Client ID is required"
            );
        }

        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than 0"
            );
        }

        if (request.getDenomination() == null ||
                request.getDenomination().signum() <= 0) {

            throw new RuntimeException(
                    "Denomination must be greater than 0"
            );
        }

        if (request.getThemeName() == null ||
                request.getThemeName().isBlank()) {

            throw new RuntimeException(
                    "Theme selection is required"
            );
        }

        if (request.getThemeImg() == null ||
                request.getThemeImg().isBlank()) {

            throw new RuntimeException(
                    "Theme image is required"
            );
        }
    }

    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================

    private TruCardCodeResponse mapToResponse(
            TruCardCode code
    ) {

        TruCardCodeResponse response =
                new TruCardCodeResponse();

        response.setId(
                code.getId()
        );

        response.setClientId(
                code.getClientId()
        );

        response.setSerialNumber(
                code.getSerialNumber()
        );

        response.setReferenceNumber(
                code.getReferenceNumber()
        );

        response.setCodeNumber(
                code.getCodeNumber()
        );

        response.setDenomination(
                code.getDenomination()
        );

        response.setStatus(
                code.getStatus()
        );

        response.setValidityMonths(
                code.getValidityMonths()
        );

        response.setThemeName(
                code.getThemeName()
        );

        response.setThemeImg(
                code.getThemeImg()
        );

        response.setActivatedAt(
                code.getActivatedAt()
        );

        response.setExpiryDate(
                code.getExpiryDate()
        );

        response.setRedeemedAt(
                code.getRedeemedAt()
        );

        response.setCreatedAt(
                code.getCreatedAt()
        );

        response.setUpdatedAt(
                code.getUpdatedAt()
        );

        return response;
    }
}