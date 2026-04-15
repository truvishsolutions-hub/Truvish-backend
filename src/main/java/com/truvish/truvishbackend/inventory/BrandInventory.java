package com.truvish.truvishbackend.inventory;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "brand_inventory")
public class BrandInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryid")
    private Long inventoryId;

    @Column(name = "inventoryvouchername")
    private String inventoryVoucherName;

    // ⭐ FINAL FIX — PostgreSQL bigint[] <-> List<Long>
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "inventoryvoucherdenomenation", columnDefinition = "bigint[]")
    private List<Long> inventoryVoucherDenomenation;


    @Column(name = "inventoryvoucherexpire")
    private LocalDate inventoryVoucherExpire;

    @Column(name = "inventoryvoucherlogourl")
    private String inventoryVoucherLogoUrl;

    @Column(name = "inventoryvoucherbrandurl")
    private String inventoryVoucherBrandUrl;

    @Column(name = "redemptionprocess")
    private String redemptionProcess;

    @Column(name = "inventoryvouchernumber")
    private String inventoryVoucherNumber;

    @Column(name = "inventoryvoucherpin")
    private String inventoryVoucherPin;

    @Column(name = "inventoryvouchercatogry")
    private String inventoryVoucherCatogry;

    // ================= GETTERS ================= //

    public Long getInventoryId() {
        return inventoryId;
    }

    public String getInventoryVoucherName() {
        return inventoryVoucherName;
    }

    // ⭐ Correct getter (capital I)
    public List<Long> getInventoryVoucherDenomenation() {
        return inventoryVoucherDenomenation;
    }

    public LocalDate getInventoryVoucherExpire() {
        return inventoryVoucherExpire;
    }

    public String getInventoryVoucherLogoUrl() {
        return inventoryVoucherLogoUrl;
    }

    public String getInventoryVoucherBrandUrl() {
        return inventoryVoucherBrandUrl;
    }

    public String getRedemptionProcess() {
        return redemptionProcess;
    }

    public String getInventoryVoucherNumber() {
        return inventoryVoucherNumber;
    }

    public String getInventoryVoucherPin() {
        return inventoryVoucherPin;
    }

    public String getInventoryVoucherCatogry() {
        return inventoryVoucherCatogry;
    }

    // ================= SETTERS ================= //

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public void setInventoryVoucherName(String inventoryVoucherName) {
        this.inventoryVoucherName = inventoryVoucherName;
    }

    public void setInventoryVoucherDenomenation(List<Long> inventoryVoucherDenomenation) {
        this.inventoryVoucherDenomenation = inventoryVoucherDenomenation;
    }

    public void setInventoryVoucherExpire(LocalDate inventoryVoucherExpire) {
        this.inventoryVoucherExpire = inventoryVoucherExpire;
    }

    public void setInventoryVoucherLogoUrl(String inventoryVoucherLogoUrl) {
        this.inventoryVoucherLogoUrl = inventoryVoucherLogoUrl;
    }

    public void setInventoryVoucherBrandUrl(String inventoryVoucherBrandUrl) {
        this.inventoryVoucherBrandUrl = inventoryVoucherBrandUrl;
    }

    public void setRedemptionProcess(String redemptionProcess) {
        this.redemptionProcess = redemptionProcess;
    }

    public void setInventoryVoucherNumber(String inventoryVoucherNumber) {
        this.inventoryVoucherNumber = inventoryVoucherNumber;
    }

    public void setInventoryVoucherPin(String inventoryVoucherPin) {
        this.inventoryVoucherPin = inventoryVoucherPin;
    }

    public void setInventoryVoucherCatogry(String inventoryVoucherCatogry) {
        this.inventoryVoucherCatogry = inventoryVoucherCatogry;
    }
}
