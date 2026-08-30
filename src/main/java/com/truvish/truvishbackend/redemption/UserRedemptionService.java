package com.truvish.truvishbackend.redemption;

import com.truvish.truvishbackend.TruBlankCode.TruBlankCode;
import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeRepository;
import com.truvish.truvishbackend.TruvishCode.TruvishCode;
import com.truvish.truvishbackend.TruvishCode.TruvishCodeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRedemptionService {

    private final UserRedemptionRepository repo;

    private final TruvishCodeRepository truvishCodeRepository;

    private final TruBlankCodeRepository truBlankCodeRepository;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UserRedemptionService(
            UserRedemptionRepository repo,
            TruvishCodeRepository truvishCodeRepository,
            TruBlankCodeRepository truBlankCodeRepository
    ) {
        this.repo = repo;
        this.truvishCodeRepository = truvishCodeRepository;
        this.truBlankCodeRepository = truBlankCodeRepository;
    }

    // =========================================================
    // BY PHONE
    // =========================================================

    public List<UserRedemption> byPhone(String phone) {

        String normalizedPhone =
                normalize(phone);

        if (isBlank(normalizedPhone)) {
            return Collections.emptyList();
        }

        return repo.findByUserPhoneNumber(
                normalizedPhone
        );
    }

    // =========================================================
    // EXISTING PHONE / CODE HISTORY
    //
    // DIGITAL HISTORY
    // =========================================================

    public List<RedemptionHistoryItemResponse> getHistory(
            String phone,
            String code
    ) {

        String normalizedPhone =
                normalize(phone);

        String normalizedCode =
                normalize(code);

        if (
                isBlank(normalizedPhone) &&
                        isBlank(normalizedCode)
        ) {
            return Collections.emptyList();
        }

        List<UserRedemption> rows;

        // =====================================================
        // PHONE + CODE
        // =====================================================

        if (
                !isBlank(normalizedPhone) &&
                        !isBlank(normalizedCode)
        ) {

            rows =
                    repo
                            .findByUserPhoneNumberOrUserTruvishCodeOrderByUserBrandTimeTempDesc(
                                    normalizedPhone,
                                    normalizedCode
                            );

        }

        // =====================================================
        // PHONE ONLY
        // =====================================================

        else if (!isBlank(normalizedPhone)) {

            rows =
                    repo
                            .findByUserPhoneNumberOrderByUserBrandTimeTempDesc(
                                    normalizedPhone
                            );

        }

        // =====================================================
        // CODE ONLY
        // =====================================================

        else {

            rows =
                    repo
                            .findByUserTruvishCodeOrderByUserBrandTimeTempDesc(
                                    normalizedCode
                            );
        }

        return convertDigitalHistory(rows);
    }

    // =========================================================
    // COMPLETE CLIENT HISTORY
    //
    // DIGITAL + PHYSICAL / TRUCARD
    //
    // GET:
    // /api/redemption-history/client/{clientId}
    // =========================================================

    public List<RedemptionHistoryItemResponse>
    getHistoryByClientId(Long clientId) {

        if (clientId == null) {
            return Collections.emptyList();
        }

        List<HistoryWrapper> allHistory =
                new ArrayList<>();

        // =====================================================
        // 1. DIGITAL HISTORY
        // =====================================================

        List<UserRedemption> digitalRows =
                getDigitalHistoryByClientId(
                        clientId
                );

        for (UserRedemption item : digitalRows) {

            RedemptionHistoryItemResponse response =
                    convertDigitalItem(item);

            if (response == null) {
                continue;
            }

            LocalDateTime date =
                    item.getUserBrandTimeTemp();

            allHistory.add(
                    new HistoryWrapper(
                            "DIGITAL",
                            normalize(
                                    item.getUserTruvishCode()
                            ),
                            date,
                            response
                    )
            );
        }

        // =====================================================
        // 2. PHYSICAL TRUCARD HISTORY
        // =====================================================

        List<TruBlankCode> physicalRows =
                truBlankCodeRepository
                        .findByClientIdOrderByCreatedAtDesc(
                                clientId
                        );

        for (TruBlankCode item : physicalRows) {

            RedemptionHistoryItemResponse response =
                    convertPhysicalItem(item);

            if (response == null) {
                continue;
            }

            LocalDateTime date =
                    getPhysicalDate(item);

            allHistory.add(
                    new HistoryWrapper(
                            "PHYSICAL",
                            normalize(
                                    item.getCodeNumber()
                            ),
                            date,
                            response
                    )
            );
        }

        // =====================================================
        // 3. SORT
        //
        // Latest transaction first
        // =====================================================

        allHistory.sort(
                Comparator.comparing(
                        HistoryWrapper::getDate,
                        Comparator.nullsLast(
                                Comparator.reverseOrder()
                        )
                )
        );

        // =====================================================
        // 4. RETURN RESPONSE
        // =====================================================

        return allHistory
                .stream()
                .map(
                        HistoryWrapper::getResponse
                )
                .collect(
                        Collectors.toList()
                );
    }

    // =========================================================
    // DIGITAL HISTORY BY CLIENT ID
    // =========================================================

    private List<UserRedemption>
    getDigitalHistoryByClientId(Long clientId) {

        if (clientId == null) {
            return Collections.emptyList();
        }

        try {

            return repo
                    .findByClientIdOrderByUserBrandTimeTempDesc(
                            clientId
                    );

        } catch (Exception e) {

            System.out.println(
                    "Digital client history query failed: "
                            + e.getMessage()
            );

            return Collections.emptyList();
        }
    }

    // =========================================================
    // CONVERT DIGITAL HISTORY
    // =========================================================

    private List<RedemptionHistoryItemResponse>
    convertDigitalHistory(
            List<UserRedemption> rows
    ) {

        if (
                rows == null ||
                        rows.isEmpty()
        ) {
            return Collections.emptyList();
        }

        Map<Long, RedemptionHistoryItemResponse>
                uniqueMap =
                new LinkedHashMap<>();

        for (UserRedemption item : rows) {

            RedemptionHistoryItemResponse response =
                    convertDigitalItem(item);

            if (response == null) {
                continue;
            }

            uniqueMap.put(
                    item.getUserId(),
                    response
            );
        }

        return uniqueMap
                .values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                RedemptionHistoryItemResponse
                                        ::getUserBrandTimeTemp,
                                Comparator.nullsLast(
                                        Comparator.reverseOrder()
                                )
                        )
                )
                .collect(
                        Collectors.toList()
                );
    }

    // =========================================================
    // CONVERT DIGITAL ITEM
    // =========================================================

    private RedemptionHistoryItemResponse
    convertDigitalItem(
            UserRedemption item
    ) {

        if (item == null) {
            return null;
        }

        // =====================================================
        // BRAND LOGO
        // =====================================================

        String brandLogo =
                item.getBrandLogo();

        if (
                brandLogo == null ||
                        brandLogo.isBlank()
        ) {

            try {

                Optional<TruvishCode> codeOpt =
                        truvishCodeRepository
                                .findByTruvishIdCodeNumber(
                                        item.getUserTruvishCode()
                                );

                if (codeOpt.isPresent()) {

                    TruvishCode codeEntity =
                            codeOpt.get();

                    brandLogo =
                            codeEntity.getClientImg();
                }

            } catch (Exception e) {

                System.out.println(
                        "Digital brand logo lookup failed: "
                                + e.getMessage()
                );
            }
        }

        // =====================================================
        // DIGITAL RESPONSE
        //
        // Physical fields intentionally null.
        // =====================================================

        return new RedemptionHistoryItemResponse(

                // COMMON
                item.getUserId(),
                item.getClientId(),
                item.getClientCompanyName(),
                item.getUserPhoneNumber(),
                item.getUserTruvishCode(),

                // BRAND
                item.getUserBrandName(),
                item.getUserBrandValue(),
                item.getUserBrandVoucher(),
                item.getUserBrandPin(),
                item.getUserBrandValidity(),

                // DATE
                item.getUserBrandTimeTemp(),

                // BALANCE
                item.getBeforeBalance(),
                item.getAfterBalance(),

                // HISTORY
                item.getHistoryMessage(),
                item.getRedeemStatus(),
                brandLogo,
                item.getRedemptionProcess(),

                // CODE TYPE
                "DIGITAL",

                // PHYSICAL
                null,       // truBlankCodeId
                null,       // serialNumber
                null,       // referenceNumber
                null,       // codeNumber
                null,       // denomination
                null,       // validityMonths
                null,       // expiryDate
                null,       // blankCodeStatus

                // CLIENT
                item.getClientCompanyName(),
                brandLogo,
                null,
                null,
                null,

                // LIFECYCLE
                null,
                null,
                null,
                null,
                null,
                null,

                // BALANCE SNAPSHOT
                item.getBeforeBalance(),
                item.getAfterBalance()
        );
    }

    // =========================================================
    // CONVERT PHYSICAL TRUCARD
    // =========================================================

    private RedemptionHistoryItemResponse
    convertPhysicalItem(
            TruBlankCode item
    ) {

        if (item == null) {
            return null;
        }

        // =====================================================
        // CODE
        // =====================================================

        String code =
                item.getCodeNumber();

        if (
                code == null ||
                        code.isBlank()
        ) {
            code = "-";
        }

        // =====================================================
        // BRAND
        // =====================================================

        String brandName = "-";

        if (
                item.getBrandNames() != null &&
                        !item.getBrandNames().isEmpty()
        ) {

            brandName =
                    String.join(
                            ", ",
                            item.getBrandNames()
                    );
        }

        // =====================================================
        // FALLBACK CLIENT BRAND
        // =====================================================

        if (
                (
                        brandName.equals("-") ||
                                brandName.isBlank()
                ) &&
                        item.getClientBrand() != null &&
                        !item.getClientBrand().isEmpty()
        ) {

            brandName =
                    String.join(
                            ", ",
                            item.getClientBrand()
                    );
        }

        // =====================================================
        // STATUS
        // =====================================================

        String status =
                item.getStatus() != null
                        ? item.getStatus().name()
                        : "INACTIVE";

        // =====================================================
        // EVENT
        // =====================================================

        String eventType =
                getPhysicalEventType(item);

        // =====================================================
        // DATE
        // =====================================================

        LocalDateTime eventDate =
                getPhysicalDate(item);

        // =====================================================
        // EXPIRY
        // =====================================================

        LocalDate expiryLocalDate = null;

        if (item.getExpiryDate() != null) {

            expiryLocalDate =
                    item.getExpiryDate()
                            .toLocalDate();
        }

        // =====================================================
        // BALANCE
        //
        // TruBlankCode = BigDecimal
        // DTO = Long
        //
        // Convert safely.
        // =====================================================

        Long beforeBalance =
                toLong(
                        item.getClientBalanceBeforeActivation()
                );

        Long afterBalance =
                toLong(
                        item.getClientBalanceAfterActivation()
                );

        // =====================================================
        // PHYSICAL RESPONSE
        // =====================================================

        return new RedemptionHistoryItemResponse(

                // COMMON
                item.getId(),
                item.getClientId(),
                item.getClientName(),
                null,
                code,

                // BRAND
                brandName,
                item.getDenomination(),
                null,
                null,
                expiryLocalDate,

                // DATE
                eventDate,

                // BALANCE
                beforeBalance,
                afterBalance,

                // HISTORY
                getPhysicalMessage(item),
                status,
                item.getClientImg(),
                eventType,

                // CODE TYPE
                "PHYSICAL",

                // PHYSICAL
                item.getId(),
                item.getSerialNumber(),
                item.getReferenceNumber(),
                item.getCodeNumber(),
                item.getDenomination(),
                item.getValidityMonths(),
                item.getExpiryDate(),
                status,

                // CLIENT
                item.getClientName(),
                item.getClientImg(),
                item.getClientCategory(),
                item.getClientTheme(),
                item.getClientThemeImg(),

                // LIFECYCLE
                item.getCreatedAt(),
                item.getUpdatedAt(),
                item.getActivatedAt(),
                item.getActivatedBy(),
                item.getRedeemedAt(),
                item.getCreatedBy(),

                // BALANCE SNAPSHOT
                beforeBalance,
                afterBalance
        );
    }

    // =========================================================
    // PHYSICAL DATE
    // =========================================================

    private LocalDateTime getPhysicalDate(
            TruBlankCode item
    ) {

        if (item.getRedeemedAt() != null) {

            return item.getRedeemedAt();
        }

        if (item.getActivatedAt() != null) {

            return item.getActivatedAt();
        }

        if (item.getUpdatedAt() != null) {

            return item.getUpdatedAt();
        }

        return item.getCreatedAt();
    }

    // =========================================================
    // PHYSICAL EVENT TYPE
    // =========================================================

    private String getPhysicalEventType(
            TruBlankCode item
    ) {

        if (item.getRedeemedAt() != null) {

            return "REDEEMED";
        }

        if (item.getActivatedAt() != null) {

            return "ACTIVATED";
        }

        if (item.getStatus() != null) {

            return item.getStatus().name();
        }

        return "CODE_ASSIGNED";
    }

    // =========================================================
    // PHYSICAL MESSAGE
    // =========================================================

    private String getPhysicalMessage(
            TruBlankCode item
    ) {

        if (item.getRedeemedAt() != null) {

            return "Physical TruCard code redeemed";
        }

        if (item.getActivatedAt() != null) {

            return "Physical TruCard code activated";
        }

        if (item.getStatus() != null) {

            String status =
                    item.getStatus().name();

            if (
                    "CANCELLED"
                            .equalsIgnoreCase(status) ||
                            "CANCELED"
                                    .equalsIgnoreCase(status)
            ) {

                return "Physical TruCard code cancelled";
            }

            if (
                    "EXPIRED"
                            .equalsIgnoreCase(status)
            ) {

                return "Physical TruCard code expired";
            }

            if (
                    "ACTIVE"
                            .equalsIgnoreCase(status)
            ) {

                return "Physical TruCard code activated";
            }

            if (
                    "INACTIVE"
                            .equalsIgnoreCase(status)
            ) {

                return "Physical TruCard issued";
            }
        }

        return "Physical TruCard issued";
    }

    // =========================================================
    // BIG DECIMAL -> LONG
    // =========================================================

    private Long toLong(
            BigDecimal value
    ) {

        if (value == null) {
            return null;
        }

        try {

            return value.longValue();

        } catch (Exception e) {

            return null;
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

    // =========================================================
    // BLANK CHECK
    // =========================================================

    private boolean isBlank(
            String value
    ) {

        return value == null ||
                value.isBlank();
    }

    // =========================================================
    // INTERNAL HISTORY WRAPPER
    // =========================================================

    private static class HistoryWrapper {

        private final String type;

        private final String code;

        private final LocalDateTime date;

        private final RedemptionHistoryItemResponse response;

        public HistoryWrapper(
                String type,
                String code,
                LocalDateTime date,
                RedemptionHistoryItemResponse response
        ) {

            this.type = type;

            this.code = code;

            this.date = date;

            this.response = response;
        }

        public String getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public RedemptionHistoryItemResponse getResponse() {
            return response;
        }
    }
}