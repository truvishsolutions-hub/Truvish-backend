package com.truvish.truvishbackend.TruvishCode;

public class CodeAssignmentDto {

    // optional
    private Long truvishId;

    // ✅ IMPORTANT — wallet debit ke liye
    private Long clientId;

    // company name
    private String clientName;

    // client logo
    private String clientImg;

    // theme
    private String clientTheme;

    // brands
    private String[] clientBrand;

    // ✅ NEW: category
    private String[] clientCategory;

    // voucher value
    private Long truvishCodeValue;

    // validity
    private Integer validity;

    // theme image
    private String clientThemeImg;

    // ======================
    // GETTERS & SETTERS
    // ======================

    public Long getTruvishId() {
        return truvishId;
    }

    public void setTruvishId(Long truvishId) {
        this.truvishId = truvishId;
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

    public String getClientTheme() {
        return clientTheme;
    }

    public void setClientTheme(String clientTheme) {
        this.clientTheme = clientTheme;
    }

    public String[] getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(String[] clientBrand) {
        this.clientBrand = clientBrand;
    }

    // ✅ NEW
    public String[] getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String[] clientCategory) {
        this.clientCategory = clientCategory;
    }

    public Long getTruvishCodeValue() {
        return truvishCodeValue;
    }

    public void setTruvishCodeValue(Long truvishCodeValue) {
        this.truvishCodeValue = truvishCodeValue;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public void setClientThemeImg(String clientThemeImg) {
        this.clientThemeImg = clientThemeImg;
    }
}