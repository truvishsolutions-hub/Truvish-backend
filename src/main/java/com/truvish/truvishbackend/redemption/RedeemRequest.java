package com.truvish.truvishbackend.redemption;

public class RedeemRequest {

    private String code;
    private String phone;
    private String brandName;
    private Long selectedValue;
    private String brandLogo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Long getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Long selectedValue) {
        this.selectedValue = selectedValue;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }
}