package com.truvish.truvishbackend.GmailDemoReq;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DemoRequest {

    // =========================================================
    // NAME
    // =========================================================

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name is too long")
    private String name;

    // =========================================================
    // EMAIL
    // =========================================================

    @NotBlank(message = "Email is required")
    @Email(message = "Valid email is required")
    @Size(max = 150, message = "Email is too long")
    private String email;

    // =========================================================
    // COMPANY
    // =========================================================

    @Size(max = 150, message = "Company name is too long")
    private String company;

    // =========================================================
    // PHONE
    // =========================================================

    @Size(max = 30, message = "Phone number is too long")
    private String phone;

    // =========================================================
    // MESSAGE
    // =========================================================

    @Size(max = 1000, message = "Message is too long")
    private String message;

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public String getName() {
        return safeTrim(name);
    }

    public void setName(String name) {
        this.name = safeTrim(name);
    }

    public String getEmail() {
        return safeTrim(email);
    }

    public void setEmail(String email) {
        this.email = safeTrim(email);
    }

    public String getCompany() {
        return safeTrim(company);
    }

    public void setCompany(String company) {
        this.company = safeTrim(company);
    }

    public String getPhone() {
        return safeTrim(phone);
    }

    public void setPhone(String phone) {
        this.phone = safeTrim(phone);
    }

    public String getMessage() {
        return safeTrim(message);
    }

    public void setMessage(String message) {
        this.message = safeTrim(message);
    }

    // =========================================================
    // SAFE TRIM
    // =========================================================

    private String safeTrim(String value) {

        return value == null
                ? null
                : value.trim();
    }
}
