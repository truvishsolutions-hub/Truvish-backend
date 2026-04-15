package com.truvish.truvishbackend.client;

import java.math.BigDecimal;

public class DashboardSummaryResponse {

    private Long totalClients;
    private Long totalUsers;
    private BigDecimal totalCurrentBalance;
    private BigDecimal totalLoadValue;
    private Long totalCodesDistributed;
    private Long totalDistributedValue;
    private Long totalRedeemedCount;
    private Long totalRedeemedAmount;

    public DashboardSummaryResponse(
            Long totalClients,
            Long totalUsers,
            BigDecimal totalCurrentBalance,
            BigDecimal totalLoadValue,
            Long totalCodesDistributed,
            Long totalDistributedValue,
            Long totalRedeemedCount,
            Long totalRedeemedAmount
    ) {
        this.totalClients = totalClients;
        this.totalUsers = totalUsers;
        this.totalCurrentBalance = totalCurrentBalance;
        this.totalLoadValue = totalLoadValue;
        this.totalCodesDistributed = totalCodesDistributed;
        this.totalDistributedValue = totalDistributedValue;
        this.totalRedeemedCount = totalRedeemedCount;
        this.totalRedeemedAmount = totalRedeemedAmount;
    }

    public Long getTotalClients() {
        return totalClients;
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public BigDecimal getTotalCurrentBalance() {
        return totalCurrentBalance;
    }

    public BigDecimal getTotalLoadValue() {
        return totalLoadValue;
    }

    public Long getTotalCodesDistributed() {
        return totalCodesDistributed;
    }

    public Long getTotalDistributedValue() {
        return totalDistributedValue;
    }

    public Long getTotalRedeemedCount() {
        return totalRedeemedCount;
    }

    public Long getTotalRedeemedAmount() {
        return totalRedeemedAmount;
    }
}