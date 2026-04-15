package com.truvish.truvishbackend.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ClientRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9]{10,15}$", message = "mobileNumber must be 10-15 digits")
    private String mobileNumber;

    @NotBlank
    @Size(max = 150)
    private String companyName;

    @NotBlank
    @Size(max = 150)
    private String clientName;

    @NotBlank
    @Email
    @Size(max = 200)
    private String email;

    // optional
    private BigDecimal balance;

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}
