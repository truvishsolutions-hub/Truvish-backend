package com.truvish.truvishbackend.voucherinventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class VoucherPinRequest {

    @NotBlank
    private String voucher;

    @NotBlank
    private String pin;

    @NotNull
    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate validityTill;

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getValidityTill() {
        return validityTill;
    }

    public void setValidityTill(LocalDate validityTill) {
        this.validityTill = validityTill;
    }
}