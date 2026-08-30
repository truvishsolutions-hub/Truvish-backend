package com.truvish.truvishbackend.TruBlankCode.response;

import com.truvish.truvishbackend.TruBlankCode.TruBlankCodeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TruBlankCodeResponse {

    // =========================================================
    // ID
    // =========================================================

    private Long id;

    // =========================================================
    // GENERATION
    // =========================================================

    private Long generationNumber;

    // =========================================================
    // CODE
    // =========================================================

    private String codeNumber;

    // =========================================================
    // SERIAL
    // =========================================================

    private String serialNumber;

    // =========================================================
    // REFERENCE
    // =========================================================

    private String referenceNumber;

    // =========================================================
    // REWARD
    // =========================================================

    private Long denomination;

    private Integer validityMonths;

    private LocalDateTime expiryDate;

    // =========================================================
    // TRUCARD BRAND
    // =========================================================

    private List<String> brandNames = new ArrayList<>();

    private String brandCategory;

    private String themeName;

    private String themeImg;

    // =========================================================
    // STATUS
    // =========================================================

    private TruBlankCodeStatus status;

    // =========================================================
    // LIFECYCLE
    // =========================================================

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime activatedAt;

    private LocalDateTime redeemedAt;

    private Long createdBy;

    private Long activatedBy;

    // =========================================================
    // CLIENT
    // =========================================================

    private Long clientId;

    private String clientName;

    private String clientImg;

    private List<String> clientBrand = new ArrayList<>();

    private String clientCategory;

    private String clientTheme;

    private String clientThemeImg;

    // =========================================================
    // BALANCE
    // =========================================================

    private BigDecimal clientBalanceBeforeActivation;

    private BigDecimal clientBalanceAfterActivation;

    // =========================================================
    // REDEEMED BY (NEW FIELD)
    // =========================================================

    private String redeemedBy;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TruBlankCodeResponse() {
        this.brandNames = new ArrayList<>();
        this.clientBrand = new ArrayList<>();
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGenerationNumber() {
        return generationNumber;
    }

    public void setGenerationNumber(Long generationNumber) {
        this.generationNumber = generationNumber;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Long getDenomination() {
        return denomination;
    }

    public void setDenomination(Long denomination) {
        this.denomination = denomination;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(Integer validityMonths) {
        this.validityMonths = validityMonths;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<String> getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(List<String> brandNames) {
        this.brandNames = brandNames != null ? new ArrayList<>(brandNames) : new ArrayList<>();
    }

    public String getBrandCategory() {
        return brandCategory;
    }

    public void setBrandCategory(String brandCategory) {
        this.brandCategory = brandCategory;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }

    public TruBlankCodeStatus getStatus() {
        return status;
    }

    public void setStatus(TruBlankCodeStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(Long activatedBy) {
        this.activatedBy = activatedBy;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientImg() {
        return clientImg;
    }

    public void setClientImg(String clientImg) {
        this.clientImg = clientImg;
    }

    public List<String> getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(List<String> clientBrand) {
        this.clientBrand = clientBrand != null ? new ArrayList<>(clientBrand) : new ArrayList<>();
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory = clientCategory;
    }

    public String getClientTheme() {
        return clientTheme;
    }

    public void setClientTheme(String clientTheme) {
        this.clientTheme = clientTheme;
    }

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public void setClientThemeImg(String clientThemeImg) {
        this.clientThemeImg = clientThemeImg;
    }

    public BigDecimal getClientBalanceBeforeActivation() {
        return clientBalanceBeforeActivation;
    }

    public void setClientBalanceBeforeActivation(BigDecimal clientBalanceBeforeActivation) {
        this.clientBalanceBeforeActivation = clientBalanceBeforeActivation;
    }

    public BigDecimal getClientBalanceAfterActivation() {
        return clientBalanceAfterActivation;
    }

    public void setClientBalanceAfterActivation(BigDecimal clientBalanceAfterActivation) {
        this.clientBalanceAfterActivation = clientBalanceAfterActivation;
    }

    // =========================================================
    // REDEEMED BY GETTER / SETTER
    // =========================================================

    public String getRedeemedBy() {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy) {
        this.redeemedBy = redeemedBy;
    }
}