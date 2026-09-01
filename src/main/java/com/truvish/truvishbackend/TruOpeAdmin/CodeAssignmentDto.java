package com.truvish.truvishbackend.TruOpeAdmin;

import jakarta.validation.constraints.*;

import java.util.List;

public class CodeAssignmentDto {

    // quantity code
    @NotNull
    @Positive
    private Integer quantity;

    // optional
    private Long truvishId;

    // ✅ IMPORTANT — wallet debit ke liye
    @NotNull
    private Long clientId;

    // company name
    @NotBlank
    private String clientName;

    // client logo
    private String clientImg;

    // theme
    private String clientTheme;

    // brands
    private List<String> clientBrand;


    // ✅ NEW: category
    private List<String> clientCategory;

    // voucher value
    @NotNull
    @Positive
    private Long truvishCodeValue;

    // validity
    @NotNull
    @Min(1)
    private Integer validity;

    // theme image
    private String clientThemeImg;


    // ======================
    // GETTERS & SETTERS
    // ======================
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

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

    public List<String> getClientBrand() {
        return clientBrand;
    }

    public void setClientCategory(List<String> clientCategory) {
        this.clientCategory = clientCategory;
    }

    // ✅ NEW
    public List<String> getClientCategory() {
        return clientCategory;
    }

    public void setClientBrand(List<String> clientBrand) {
        this.clientBrand = clientBrand;
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