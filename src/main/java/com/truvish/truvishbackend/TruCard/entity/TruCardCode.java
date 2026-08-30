package com.truvish.truvishbackend.TruCard.entity;

import com.truvish.truvishbackend.TruCard.enums.TruCardCodeStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "trucard_codes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_trucard_code_number",
                        columnNames = "code_number"
                ),
                @UniqueConstraint(
                        name = "uk_trucard_serial_number",
                        columnNames = "serial_number"
                ),
                @UniqueConstraint(
                        name = "uk_trucard_reference_number",
                        columnNames = "reference_number"
                )
        },
        indexes = {
                @Index(
                        name = "idx_trucard_code_client",
                        columnList = "client_id"
                ),
                @Index(
                        name = "idx_trucard_code_order",
                        columnList = "order_id"
                ),
                @Index(
                        name = "idx_trucard_code_campaign",
                        columnList = "campaign_id"
                ),
                @Index(
                        name = "idx_trucard_code_status",
                        columnList = "status"
                )
        }
)
public class TruCardCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================================================
    // CLIENT
    // =========================================================

    @Column(name = "client_id", nullable = false)
    private Long clientId;


    // =========================================================
    // ORDER
    // =========================================================

    @Column(name = "order_id")
    private Long orderId;


    // =========================================================
    // CAMPAIGN
    // =========================================================

    @Column(name = "campaign_id")
    private Long campaignId;


    // =========================================================
    // SERIAL NUMBER
    // =========================================================

    @Column(
            name = "serial_number",
            nullable = false,
            unique = true,
            length = 12
    )
    private String serialNumber;


    // =========================================================
    // REFERENCE NUMBER
    // =========================================================

    @Column(
            name = "reference_number",
            nullable = false,
            unique = true,
            length = 30
    )
    private String referenceNumber;


    // =========================================================
    // REDEEM CODE
    // =========================================================

    @Column(
            name = "code_number",
            nullable = false,
            unique = true,
            length = 20
    )
    private String codeNumber;


    // =========================================================
    // DENOMINATION
    // =========================================================

    @Column(
            name = "denomination",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal denomination;


    // =========================================================
    // STATUS
    // =========================================================

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 50
    )
    private TruCardCodeStatus status;


    // =========================================================
    // VALIDITY
    // =========================================================

    @Column(name = "validity_months")
    private Integer validityMonths;


    // =========================================================
    // THEME
    // =========================================================

    @Column(
            name = "theme_name",
            length = 255
    )
    private String themeName;


    @Column(
            name = "theme_img",
            length = 500
    )
    private String themeImg;


    // =========================================================
    // ACTIVATION
    // =========================================================

    @Column(name = "activated_at")
    private LocalDateTime activatedAt;


    // =========================================================
    // EXPIRY
    // =========================================================

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;


    // =========================================================
    // REDEEMED
    // =========================================================

    @Column(name = "redeemed_at")
    private LocalDateTime redeemedAt;


    // =========================================================
    // CREATED / UPDATED
    // =========================================================

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // =========================================================
    // PRE PERSIST
    // =========================================================

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        if (status == null) {
            status = TruCardCodeStatus.INACTIVE;
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }


    // =========================================================
    // PRE UPDATE
    // =========================================================

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
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


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }


    public Long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Long campaignId) {
        this.campaignId = campaignId;
    }


    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }


    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }


    public TruCardCodeStatus getStatus() {
        return status;
    }

    public void setStatus(TruCardCodeStatus status) {
        this.status = status;
    }


    public Integer getValidityMonths() {
        return validityMonths;
    }

    public void setValidityMonths(Integer validityMonths) {
        this.validityMonths = validityMonths;
    }


    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }


    public String getThemeImg() {
        return themeImg;
    }

    public void setThemeImg(String themeImg) {
        this.themeImg = themeImg;
    }


    public LocalDateTime getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(LocalDateTime activatedAt) {
        this.activatedAt = activatedAt;
    }


    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }


    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}