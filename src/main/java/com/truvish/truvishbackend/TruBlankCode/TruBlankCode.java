package com.truvish.truvishbackend.TruBlankCode;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "truvish_blank_code",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_blank_code_number",
                        columnNames = "code_number"
                ),
                @UniqueConstraint(
                        name = "uk_blank_serial_number",
                        columnNames = "serial_number"
                ),
                @UniqueConstraint(
                        name = "uk_blank_reference_number",
                        columnNames = "reference_number"
                )
        }
)
public class TruBlankCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // CODE INFORMATION
    // =========================================================

    @Column(
            name = "generation_number",
            nullable = false
    )
    private Long generationNumber;

    @Column(
            name = "code_number",
            nullable = false,
            length = 20
    )
    private String codeNumber;

    @Column(
            name = "serial_number",
            nullable = false,
            length = 30
    )
    private String serialNumber;

    @Column(
            name = "reference_number",
            nullable = false,
            length = 100
    )
    private String referenceNumber;

    // =========================================================
    // REWARD
    // =========================================================

    @Column(name = "denomination")
    private Long denomination;

    @Column(name = "validity_months")
    private Integer validityMonths;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    // =========================================================
    // TRUCARD BRANDS
    // =========================================================

    @ElementCollection
    @CollectionTable(
            name = "truvish_blank_code_brands",
            joinColumns = @JoinColumn(
                    name = "blank_code_id"
            )
    )
    @Column(
            name = "brand_name",
            nullable = false,
            length = 150
    )
    private List<String> brandNames =
            new ArrayList<>();

    @Column(
            name = "brand_category",
            length = 100
    )
    private String brandCategory;

    // =========================================================
    // TRUCARD THEME
    // =========================================================

    @Column(
            name = "theme_name",
            length = 150
    )
    private String themeName;

    @Column(
            name = "theme_img",
            columnDefinition = "TEXT"
    )
    private String themeImg;

    // =========================================================
    // STATUS
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private TruBlankCodeStatus status;

    // =========================================================
    // LIFECYCLE
    // =========================================================

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;

    @Column(name = "activated_by")
    private Long activatedBy;

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;

    @Column(name = "created_by")
    private Long createdBy;

    // =========================================================
    // CLIENT
    // =========================================================

    @Column(name = "client_id")
    private Long clientId;

    @Column(
            name = "client_name",
            length = 200
    )
    private String clientName;

    @Column(
            name = "client_img",
            columnDefinition = "TEXT"
    )
    private String clientImg;

    // =========================================================
    // CLIENT BRANDS
    // =========================================================

    @ElementCollection
    @CollectionTable(
            name = "truvish_blank_code_client_brands",
            joinColumns = @JoinColumn(
                    name = "blank_code_id"
            )
    )
    @Column(
            name = "client_brand",
            nullable = false,
            length = 150
    )
    private List<String> clientBrand =
            new ArrayList<>();

    @Column(
            name = "client_category",
            length = 100
    )
    private String clientCategory;

    // =========================================================
    // CLIENT THEME
    // =========================================================

    @Column(
            name = "client_theme",
            length = 150
    )
    private String clientTheme;

    @Column(
            name = "client_theme_img",
            columnDefinition = "TEXT"
    )
    private String clientThemeImg;

    // =========================================================
    // BALANCE SNAPSHOT
    // =========================================================

    @Column(
            name = "client_balance_before_activation",
            precision = 19,
            scale = 2
    )
    private BigDecimal clientBalanceBeforeActivation;

    @Column(
            name = "client_balance_after_activation",
            precision = 19,
            scale = 2
    )
    private BigDecimal clientBalanceAfterActivation;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TruBlankCode() {

        this.brandNames =
                new ArrayList<>();

        this.clientBrand =
                new ArrayList<>();
    }

    // =========================================================
    // PRE PERSIST
    // =========================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (status == null) {
            status =
                    TruBlankCodeStatus.INACTIVE;
        }

        if (brandNames == null) {
            brandNames =
                    new ArrayList<>();
        }

        if (clientBrand == null) {
            clientBrand =
                    new ArrayList<>();
        }
    }

    // =========================================================
    // PRE UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();

        if (brandNames == null) {
            brandNames =
                    new ArrayList<>();
        }

        if (clientBrand == null) {
            clientBrand =
                    new ArrayList<>();
        }
    }

    // =========================================================
    // GETTERS / SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGenerationNumber() {
        return generationNumber;
    }

    public void setGenerationNumber(
            Long generationNumber
    ) {
        this.generationNumber =
                generationNumber;
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(
            String codeNumber
    ) {
        this.codeNumber =
                codeNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(
            String serialNumber
    ) {
        this.serialNumber =
                serialNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(
            String referenceNumber
    ) {
        this.referenceNumber =
                referenceNumber;
    }

    public Long getDenomination() {
        return denomination;
    }

    public void setDenomination(
            Long denomination
    ) {
        this.denomination =
                denomination;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(
            Integer validityMonths
    ) {
        this.validityMonths =
                validityMonths;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(
            LocalDateTime expiryDate
    ) {
        this.expiryDate =
                expiryDate;
    }

    public List<String> getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(
            List<String> brandNames
    ) {
        this.brandNames =
                brandNames != null
                        ? new ArrayList<>(brandNames)
                        : new ArrayList<>();
    }

    public String getBrandCategory() {
        return brandCategory;
    }

    public void setBrandCategory(
            String brandCategory
    ) {
        this.brandCategory =
                brandCategory;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(
            String themeName
    ) {
        this.themeName =
                themeName;
    }

    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(
            String themeImg
    ) {
        this.themeImg =
                themeImg;
    }

    public TruBlankCodeStatus getStatus() {
        return status;
    }

    public void setStatus(
            TruBlankCodeStatus status
    ) {
        this.status =
                status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            LocalDateTime createdAt
    ) {
        this.createdAt =
                createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt
    ) {
        this.updatedAt =
                updatedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(
            LocalDateTime assignedAt
    ) {
        this.assignedAt =
                assignedAt;
    }

    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(
            LocalDateTime activatedAt
    ) {
        this.activatedAt =
                activatedAt;
    }

    public Long getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(
            Long activatedBy
    ) {
        this.activatedBy =
                activatedBy;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(
            LocalDateTime redeemedAt
    ) {
        this.redeemedAt =
                redeemedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy =
                createdBy;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId =
                clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName =
                clientName;
    }

    public String getClientImg() {
        return clientImg;
    }

    public void setClientImg(String clientImg) {
        this.clientImg =
                clientImg;
    }

    public List<String> getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(
            List<String> clientBrand
    ) {
        this.clientBrand =
                clientBrand != null
                        ? new ArrayList<>(clientBrand)
                        : new ArrayList<>();
    }

    public String getClientCategory() {
        return clientCategory;
    }

    public void setClientCategory(String clientCategory) {
        this.clientCategory =
                clientCategory;
    }

    public String getClientTheme() {
        return clientTheme;
    }

    public void setClientTheme(String clientTheme) {
        this.clientTheme =
                clientTheme;
    }

    public String getClientThemeImg() {
        return clientThemeImg;
    }

    public void setClientThemeImg(String clientThemeImg) {
        this.clientThemeImg =
                clientThemeImg;
    }

    public BigDecimal getClientBalanceBeforeActivation() {
        return clientBalanceBeforeActivation;
    }

    public void setClientBalanceBeforeActivation(
            BigDecimal value
    ) {
        this.clientBalanceBeforeActivation =
                value;
    }

    public BigDecimal getClientBalanceAfterActivation() {
        return clientBalanceAfterActivation;
    }

    public void setClientBalanceAfterActivation(
            BigDecimal value
    ) {
        this.clientBalanceAfterActivation =
                value;
    }
}