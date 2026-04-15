package com.truvish.truvishbackend.voucherinventory;

import com.truvish.truvishbackend.ClientChooseBrand.ClientChooseBrandRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VoucherInventoryService {

    private final VoucherInventoryRepository voucherInventoryRepository;
    private final VoucherRedeemedLogRepository voucherRedeemedLogRepository;
    private final ClientChooseBrandRepository clientChooseBrandRepository;

    public VoucherInventoryService(
            VoucherInventoryRepository voucherInventoryRepository,
            VoucherRedeemedLogRepository voucherRedeemedLogRepository,
            ClientChooseBrandRepository clientChooseBrandRepository
    ) {
        this.voucherInventoryRepository = voucherInventoryRepository;
        this.voucherRedeemedLogRepository = voucherRedeemedLogRepository;
        this.clientChooseBrandRepository = clientChooseBrandRepository;
    }

    @Transactional
    public List<VoucherInventory> addInventory(AddVoucherInventoryRequest request) {
        String brandName = normalize(request.getBrandName());
        BigDecimal denomination = request.getDenomination();
        String redemptionProcess = normalizeMultiline(request.getRedemptionProcess());

        if (brandName == null || brandName.isBlank()) {
            throw new RuntimeException("Brand name is required");
        }

        if (!clientChooseBrandRepository.existsByBrandNameIgnoreCase(brandName)) {
            throw new RuntimeException("Selected brand is not available in ClientChooseBrand: " + brandName);
        }

        if (denomination == null || denomination.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Denomination must be greater than 0");
        }

        List<VoucherPinRequest> vouchers = request.getAddVouchers();
        if (vouchers == null || vouchers.isEmpty()) {
            throw new RuntimeException("At least one voucher and pin is required");
        }

        List<VoucherInventory> rows = vouchers.stream()
                .map(v -> buildInventoryRow(brandName, denomination, redemptionProcess, v))
                .toList();

        return voucherInventoryRepository.saveAll(rows);
    }

    public List<VoucherInventorySummaryResponse> getSummary() {
        Map<String, List<VoucherInventory>> grouped = voucherInventoryRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(v ->
                        normalize(v.getBrandName()) + "__"
                                + v.getDenomination().stripTrailingZeros().toPlainString() + "__"
                                + v.getValidityTill() + "__"
                                + normalize(v.getRedemptionProcess())
                ));

        return grouped.values()
                .stream()
                .map(this::toSummary)
                .sorted(
                        Comparator.comparing(VoucherInventorySummaryResponse::getBrandName, String.CASE_INSENSITIVE_ORDER)
                                .thenComparing(VoucherInventorySummaryResponse::getDenomination)
                                .thenComparing(VoucherInventorySummaryResponse::getValidityTill)
                )
                .toList();
    }

    public List<String> getAvailableBrands() {
        return clientChooseBrandRepository.findAllByOrderByBrandNameAsc()
                .stream()
                .map(b -> normalize(b.getBrandName()))
                .distinct()
                .toList();
    }

    public List<BigDecimal> getAvailableDenominations(String brandName) {
        return voucherInventoryRepository
                .findByBrandNameIgnoreCaseOrderByDenominationAscValidityTillAscCreatedAtAsc(brandName)
                .stream()
                .filter(v -> "ACTIVE".equalsIgnoreCase(v.getStatus()))
                .map(VoucherInventory::getDenomination)
                .distinct()
                .toList();
    }

    public List<VoucherInventoryCounterResponse> getCounter(String brandName, BigDecimal denomination) {
        return voucherInventoryRepository
                .findByBrandNameIgnoreCaseAndDenominationOrderByValidityTillAscCreatedAtAsc(brandName, denomination)
                .stream()
                .map(v -> new VoucherInventoryCounterResponse(
                        v.getId(),
                        v.getBrandName(),
                        v.getDenomination(),
                        v.getVoucherCode(),
                        v.getVoucherPin(),
                        v.getValidityTill(),
                        v.getStatus(),
                        v.getUsedAt()
                ))
                .toList();
    }

    @Transactional
    public void deleteVoucher(Long id) {
        VoucherInventory voucher = voucherInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucherInventoryRepository.delete(voucher);
    }

    @Transactional
    public VoucherRedeemResponse redeem(VoucherRedeemRequest request) {
        VoucherInventory voucherInventory = voucherInventoryRepository
                .findFirstByBrandNameIgnoreCaseAndDenominationAndStatusIgnoreCaseAndValidityTillGreaterThanEqualOrderByValidityTillAscCreatedAtAsc(
                        request.getBrandName(),
                        request.getDenomination(),
                        "ACTIVE",
                        LocalDate.now()
                )
                .orElseThrow(() -> new RuntimeException(
                        "No available active voucher found for brand "
                                + request.getBrandName()
                                + " and denomination "
                                + request.getDenomination()
                ));

        LocalDateTime now = LocalDateTime.now();

        VoucherRedeemedLog log = new VoucherRedeemedLog();
        log.setInventoryId(voucherInventory.getId());
        log.setBrandName(voucherInventory.getBrandName());
        log.setDenomination(voucherInventory.getDenomination());
        log.setVoucherCode(voucherInventory.getVoucherCode());
        log.setVoucherPin(voucherInventory.getVoucherPin());
        log.setValidityTill(voucherInventory.getValidityTill());
        log.setRedeemedAt(now);
        log.setRedeemedBy(blankToNull(request.getRedeemedBy()));
        log.setOrderReference(blankToNull(request.getOrderReference()));
        voucherRedeemedLogRepository.save(log);

        voucherInventory.setStatus("USED");
        voucherInventory.setUsedAt(now);
        voucherInventory.setUsedBy(blankToNull(request.getRedeemedBy()));
        voucherInventory.setUsedOrderReference(blankToNull(request.getOrderReference()));
        voucherInventoryRepository.save(voucherInventory);

        return new VoucherRedeemResponse(
                log.getBrandName(),
                log.getDenomination(),
                log.getVoucherCode(),
                log.getVoucherPin(),
                log.getValidityTill(),
                log.getRedeemedAt()
        );
    }

    private VoucherInventory buildInventoryRow(
            String brandName,
            BigDecimal denomination,
            String redemptionProcess,
            VoucherPinRequest voucherPinRequest
    ) {
        String voucherCode = normalize(voucherPinRequest.getVoucher());
        String voucherPin = normalize(voucherPinRequest.getPin());
        LocalDate validityTill = voucherPinRequest.getValidityTill();

        if (voucherCode == null || voucherCode.isBlank()) {
            throw new RuntimeException("Voucher code is required");
        }

        if (voucherPin == null || voucherPin.isBlank()) {
            throw new RuntimeException("Voucher pin is required");
        }

        if (validityTill == null) {
            throw new RuntimeException("Validity date is required for each voucher");
        }

        if (validityTill.isBefore(LocalDate.now())) {
            throw new RuntimeException("Validity date cannot be in the past for voucher: " + voucherCode);
        }

        if (voucherInventoryRepository.existsByVoucherCodeIgnoreCase(voucherCode)) {
            throw new RuntimeException("Voucher already exists in inventory: " + voucherCode);
        }

        VoucherInventory row = new VoucherInventory();
        row.setBrandName(brandName);
        row.setDenomination(denomination);
        row.setVoucherCode(voucherCode);
        row.setVoucherPin(voucherPin);
        row.setValidityTill(validityTill);
        row.setRedemptionProcess(redemptionProcess);
        row.setStatus("ACTIVE");
        return row;
    }

    private VoucherInventorySummaryResponse toSummary(List<VoucherInventory> rows) {
        VoucherInventory first = rows.get(0);

        long totalCount = rows.size();
        long activeCount = rows.stream().filter(v -> "ACTIVE".equalsIgnoreCase(v.getStatus())).count();
        long usedCount = rows.stream().filter(v -> "USED".equalsIgnoreCase(v.getStatus())).count();
        BigDecimal total = first.getDenomination().multiply(BigDecimal.valueOf(totalCount));

        return new VoucherInventorySummaryResponse(
                first.getBrandName(),
                first.getDenomination(),
                first.getValidityTill(),
                totalCount,
                activeCount,
                usedCount,
                total
        );
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private String normalizeMultiline(String value) {
        if (value == null) return null;
        String normalized = value.replace("\r\n", "\n").trim();
        return normalized.isBlank() ? null : normalized;
    }

    private String blankToNull(String value) {
        String normalized = normalize(value);
        return normalized == null || normalized.isBlank() ? null : normalized;
    }
}