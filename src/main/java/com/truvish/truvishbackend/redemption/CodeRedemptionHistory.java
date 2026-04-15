package com.truvish.truvishbackend.redemption;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "code_redemption_history",
        indexes = {
                @Index(name = "idx_code_redemption_code", columnList = "truvish_code"),
                @Index(name = "idx_code_redemption_phone", columnList = "phone_number"),
                @Index(name = "idx_code_redemption_time", columnList = "redeemed_at")
        }
)
public class CodeRedemptionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "client_company_name", length = 200)
    private String clientCompanyName;

    @Column(name = "truvish_code", nullable = false, length = 100)
    private String truvishCode;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "brand_name", nullable = false, length = 150)
    private String brandName;

    @Column(name = "redeemed_value", nullable = false)
    private Long redeemedValue;

    @Column(name = "voucher_code", length = 255)
    private String voucherCode;

    @Column(name = "voucher_pin", length = 255)
    private String voucherPin;

    @Column(name = "validity_till")
    private LocalDate validityTill;

    @Column(name = "before_balance")
    private Long beforeBalance;

    @Column(name = "after_balance")
    private Long afterBalance;

    @Column(name = "redeem_status", length = 50)
    private String redeemStatus;

    @Column(name = "history_message", length = 255)
    private String historyMessage;

    @Column(name = "brand_logo", length = 500)
    private String brandLogo;

    @Column(name = "redemption_process", columnDefinition = "text")
    private String redemptionProcess;

    @Column(name = "redeemed_at", nullable = false)
    private LocalDateTime redeemedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (redeemedAt == null) {
            redeemedAt = LocalDateTime.now();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientCompanyName() {
        return clientCompanyName;
    }

    public void setClientCompanyName(String clientCompanyName) {
        this.clientCompanyName = clientCompanyName;
    }

    public String getTruvishCode() {
        return truvishCode;
    }

    public void setTruvishCode(String truvishCode) {
        this.truvishCode = truvishCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getRedeemedValue() {
        return redeemedValue;
    }

    public void setRedeemedValue(Long redeemedValue) {
        this.redeemedValue = redeemedValue;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getVoucherPin() {
        return voucherPin;
    }

    public void setVoucherPin(String voucherPin) {
        this.voucherPin = voucherPin;
    }

    public LocalDate getValidityTill() {
        return validityTill;
    }

    public void setValidityTill(LocalDate validityTill) {
        this.validityTill = validityTill;
    }

    public Long getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(Long beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getHistoryMessage() {
        return historyMessage;
    }

    public void setHistoryMessage(String historyMessage) {
        this.historyMessage = historyMessage;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }

    public void setRedemptionProcess(String redemptionProcess) {
        this.redemptionProcess = redemptionProcess;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}