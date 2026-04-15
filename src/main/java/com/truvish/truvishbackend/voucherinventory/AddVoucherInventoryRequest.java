package com.truvish.truvishbackend.voucherinventory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class AddVoucherInventoryRequest {

    @NotBlank
    private String brandName;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal denomination;

    private String redemptionProcess;

    @NotEmpty
    @Valid
    private List<VoucherPinRequest> addVouchers;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }

    public void setRedemptionProcess(String redemptionProcess) {
        this.redemptionProcess = redemptionProcess;
    }

    public List<VoucherPinRequest> getAddVouchers() {
        return addVouchers;
    }

    public void setAddVouchers(List<VoucherPinRequest> addVouchers) {
        this.addVouchers = addVouchers;
    }
}