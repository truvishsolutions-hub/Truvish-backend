package com.truvish.truvishbackend.ClientEmailService;

public class VoucherRequest {

    private String email;

    private String name;

    private String voucherCode;

    // ONLY FILE NAME
    // Example: nike.png
    private String clientLogo;

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getClientLogo() {
        return clientLogo;
    }

    public void setClientLogo(String clientLogo) {
        this.clientLogo = clientLogo;
    }
}