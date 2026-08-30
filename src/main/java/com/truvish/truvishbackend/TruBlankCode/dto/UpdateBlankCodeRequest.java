package com.truvish.truvishbackend.TruBlankCode.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

public class UpdateBlankCodeRequest {

    // =========================================================
    // DENOMINATION
    // =========================================================

    @DecimalMin(
            value = "0.01",
            message = "Denomination must be greater than 0"
    )
    private Double denomination;


    // =========================================================
    // VALIDITY MONTHS
    // =========================================================

    @Min(
            value = 1,
            message = "Validity months must be greater than 0"
    )
    private Integer validityMonths;


    // =========================================================
    // BRAND NAMES
    // =========================================================

    private List<String> brandNames;


    // =========================================================
    // BRAND CATEGORY
    // =========================================================

    private String brandCategory;


    // =========================================================
    // THEME NAME
    // =========================================================

    private String themeName;


    // =========================================================
    // THEME IMAGE
    // =========================================================

    private String themeImg;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UpdateBlankCodeRequest() {
    }


    // =========================================================
    // DENOMINATION
    // =========================================================

    public Double getDenomination() {
        return denomination;
    }

    public void setDenomination(Double denomination) {
        this.denomination = denomination;
    }


    // =========================================================
    // VALIDITY MONTHS
    // =========================================================

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(Integer validityMonths) {
        this.validityMonths = validityMonths;
    }


    // =========================================================
    // BRAND NAMES
    // =========================================================

    public List<String> getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(List<String> brandNames) {

        if (brandNames == null) {
            this.brandNames = null;
            return;
        }

        this.brandNames =
                new ArrayList<>(brandNames);
    }


    // =========================================================
    // BRAND CATEGORY
    // =========================================================

    public String getBrandCategory() {
        return brandCategory;
    }

    public void setBrandCategory(String brandCategory) {
        this.brandCategory = brandCategory;
    }


    // =========================================================
    // THEME NAME
    // =========================================================

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }


    // =========================================================
    // THEME IMAGE
    // =========================================================

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }
}