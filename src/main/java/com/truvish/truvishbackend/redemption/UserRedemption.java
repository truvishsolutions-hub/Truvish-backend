package com.truvish.truvishbackend.redemption;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_redemption")
public class UserRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long clientId;
    private String clientCompanyName;

    private String userPhoneNumber;
    private String userTruvishCode;
    private String userBrandName;
    private Long userBrandValue;

    @Column(name = "user_brand_voucher")
    private String userBrandVoucher;

    private String userBrandPin;
    private LocalDate userBrandValidity;
    private LocalDateTime userBrandTimeTemp;

    private Long beforeBalance;
    private Long afterBalance;
    private String historyMessage;
    private String redeemStatus;

    @Column(name = "brand_logo")
    private String brandLogo;

    @Column(name = "redemption_process", columnDefinition = "text")
    private String redemptionProcess;

    public Long getUserId() {
        return userId;
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

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserTruvishCode() {
        return userTruvishCode;
    }

    public void setUserTruvishCode(String userTruvishCode) {
        this.userTruvishCode = userTruvishCode;
    }

    public String getUserBrandName() {
        return userBrandName;
    }

    public void setUserBrandName(String userBrandName) {
        this.userBrandName = userBrandName;
    }

    public Long getUserBrandValue() {
        return userBrandValue;
    }

    public void setUserBrandValue(Long userBrandValue) {
        this.userBrandValue = userBrandValue;
    }

    public String getUserBrandVoucher() {
        return userBrandVoucher;
    }

    public void setUserBrandVoucher(String userBrandVoucher) {
        this.userBrandVoucher = userBrandVoucher;
    }

    public String getUserBrandPin() {
        return userBrandPin;
    }

    public void setUserBrandPin(String userBrandPin) {
        this.userBrandPin = userBrandPin;
    }

    public LocalDate getUserBrandValidity() {
        return userBrandValidity;
    }

    public void setUserBrandValidity(LocalDate userBrandValidity) {
        this.userBrandValidity = userBrandValidity;
    }

    public LocalDateTime getUserBrandTimeTemp() {
        return userBrandTimeTemp;
    }

    public void setUserBrandTimeTemp(LocalDateTime userBrandTimeTemp) {
        this.userBrandTimeTemp = userBrandTimeTemp;
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

    public String getHistoryMessage() {
        return historyMessage;
    }

    public void setHistoryMessage(String historyMessage) {
        this.historyMessage = historyMessage;
    }

    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus;
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
}