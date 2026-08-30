package com.truvish.truvishbackend.TruCard.dto;

import java.math.BigDecimal;

public class TruCardCodeRequest {

    // =========================================================
    // CLIENT
    // =========================================================

    private Long clientId;


    // =========================================================
    // QUANTITY
    // =========================================================

    private Integer quantity;


    // =========================================================
    // CARD VALUE
    // =========================================================

    private BigDecimal denomination;


    // =========================================================
    // THEME
    // =========================================================

    private String themeName;

    private String themeImg;


    // =========================================================
    // GETTERS
    // =========================================================

    public Long getClientId() {
        return clientId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getThemeImg() {
        return themeImg;
    }


    // =========================================================
    // SETTERS
    // =========================================================

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }
}