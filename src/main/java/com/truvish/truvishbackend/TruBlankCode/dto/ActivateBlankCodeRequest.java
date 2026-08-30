package com.truvish.truvishbackend.TruBlankCode.dto;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ActivateBlankCodeRequest {

    // =========================================================
    // ADMIN
    // =========================================================

    @NotNull(message = "adminId is required")
    private Long adminId;


    // =========================================================
    // CLIENT
    // =========================================================

    @NotNull(message = "clientId is required")
    private Long clientId;

    private String clientName;

    private String clientImg;

    private List<String> clientBrand =
            new ArrayList<>();

    private String clientCategory;

    private String clientTheme;

    private String clientThemeImg;


    // =========================================================
    // VALIDITY
    // =========================================================

    private Integer validityMonths;


    // =========================================================
    // ADMIN ID
    // =========================================================

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }


    // =========================================================
    // CLIENT ID
    // =========================================================

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    // =========================================================
    // CLIENT NAME
    // =========================================================

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    // =========================================================
    // CLIENT IMAGE
    // =========================================================

    public String getClientImg() {
        return clientImg;
    }

    public void setClientImg(String clientImg) {
        this.clientImg = clientImg;
    }


    // =========================================================
    // CLIENT BRAND
    // =========================================================

    public List<String> getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(
            List<String> clientBrand
    ) {

        this.clientBrand =
                clientBrand != null
                        ? new ArrayList<>(clientBrand)
                        : new ArrayList<>();
    }


    // =========================================================
    // CLIENT CATEGORY
    // =========================================================

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(
            String clientCategory
    ) {
        this.clientCategory = clientCategory;
    }


    // =========================================================
    // CLIENT THEME
    // =========================================================

    public String getClientTheme() {
        return clientTheme;
    }

    public void setClientTheme(
            String clientTheme
    ) {
        this.clientTheme = clientTheme;
    }


    // =========================================================
    // CLIENT THEME IMAGE
    // =========================================================

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public void setClientThemeImg(
            String clientThemeImg
    ) {
        this.clientThemeImg = clientThemeImg;
    }


    // =========================================================
    // VALIDITY MONTHS
    // =========================================================

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(
            Integer validityMonths
    ) {
        this.validityMonths = validityMonths;
    }
}