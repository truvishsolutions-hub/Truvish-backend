package com.truvish.truvishbackend.TruOpeAdmin;

import java.util.List;

public class VerifyCodeResponse {

    private String code;
    private String status;
    private String phone;
    private Long value;
    private String clientImg;
    private Integer validity;
    private String clientThemeImg;
    private List<String> clientBrand;
    private List<String> clientCategory;

    public VerifyCodeResponse(
            String code,
            String status,
            String phone,
            Long value,
            String clientImg,
            Integer validity,
            String clientThemeImg,
            List<String> clientBrand,
            List<String> clientCategory
    ) {
        this.code = code;
        this.status = status;
        this.phone = phone;
        this.value = value;
        this.clientImg = clientImg;
        this.validity = validity;
        this.clientThemeImg = clientThemeImg;
        this.clientBrand = clientBrand;
        this.clientCategory = clientCategory;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public Long getValue() {
        return value;
    }

    public String getClientImg() {
        return clientImg;
    }

    public Integer getValidity() {
        return validity;
    }

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public List<String> getClientBrand() {
        return clientBrand;
    }

    public List<String> getClientCategory() {
        return clientCategory;
    }
}