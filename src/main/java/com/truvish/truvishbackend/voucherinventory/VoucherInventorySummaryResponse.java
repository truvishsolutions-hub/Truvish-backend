package com.truvish.truvishbackend.voucherinventory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VoucherInventorySummaryResponse {

    private String brandName;
    private BigDecimal denomination;
    private LocalDate validityTill;
    private long totalCount;
    private long activeCount;
    private long usedCount;
    private BigDecimal total;

    public VoucherInventorySummaryResponse(
            String brandName,
            BigDecimal denomination,
            LocalDate validityTill,
            long totalCount,
            long activeCount,
            long usedCount,
            BigDecimal total
    ) {
        this.brandName = brandName;
        this.denomination = denomination;
        this.validityTill = validityTill;
        this.totalCount = totalCount;
        this.activeCount = activeCount;
        this.usedCount = usedCount;
        this.total = total;
    }

    public String getBrandName() {
        return brandName;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public LocalDate getValidityTill() {
        return validityTill;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public long getActiveCount() {
        return activeCount;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public BigDecimal getTotal() {
        return total;
    }
}