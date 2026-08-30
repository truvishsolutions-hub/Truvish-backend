package com.truvish.truvishbackend.ClientEmailService;

import java.util.List;

public class VoucherRequest {

    private String email;
    private String name;
    private String voucherCode;
    private String clientLogo;
    private Integer validityDays;
    private String companyName;  // 🔥 ADD THIS

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }

    public String getClientLogo() { return clientLogo; }
    public void setClientLogo(String clientLogo) { this.clientLogo = clientLogo; }

    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }

    public String getCompanyName() { return companyName; }  // 🔥 ADD THIS
    public void setCompanyName(String companyName) { this.companyName = companyName; }  // 🔥 ADD THIS
}