package com.truvish.truvishbackend.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClientOverviewResponse {

    private Long id;
    private String mobileNumber;
    private String companyName;
    private String clientName;
    private String email;
    private String logoImg;
    private BigDecimal currentBalance;
    private BigDecimal totalLoad;
    private Long codesDistributed;
    private Long distributedValue;
    private Long redeemedCount;
    private Long redeemedAmount;
    private LocalDateTime createdAt;

    public ClientOverviewResponse(
            Long id,
            String mobileNumber,
            String companyName,
            String clientName,
            String email,
            String logoImg,
            BigDecimal currentBalance,
            BigDecimal totalLoad,
            Long codesDistributed,
            Long distributedValue,
            Long redeemedCount,
            Long redeemedAmount,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.companyName = companyName;
        this.clientName = clientName;
        this.email = email;
        this.logoImg = logoImg;
        this.currentBalance = currentBalance;
        this.totalLoad = totalLoad;
        this.codesDistributed = codesDistributed;
        this.distributedValue = distributedValue;
        this.redeemedCount = redeemedCount;
        this.redeemedAmount = redeemedAmount;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getMobileNumber() { return mobileNumber; }
    public String getCompanyName() { return companyName; }
    public String getClientName() { return clientName; }
    public String getEmail() { return email; }
    public String getLogoImg() { return logoImg; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public BigDecimal getTotalLoad() { return totalLoad; }
    public Long getCodesDistributed() { return codesDistributed; }
    public Long getDistributedValue() { return distributedValue; }
    public Long getRedeemedCount() { return redeemedCount; }
    public Long getRedeemedAmount() { return redeemedAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}