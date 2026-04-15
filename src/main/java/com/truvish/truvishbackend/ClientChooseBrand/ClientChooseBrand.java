package com.truvish.truvishbackend.ClientChooseBrand;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_choose_brand")
public class ClientChooseBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String category;

    @Column
    private String brandImg;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String howToRedeem;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public ClientChooseBrand() {
    }

    public ClientChooseBrand(
            Long id,
            String brandName,
            String category,
            String brandImg,
            String termsAndConditions,
            String howToRedeem,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.brandName = brandName;
        this.category = category;
        this.brandImg = brandImg;
        this.termsAndConditions = termsAndConditions;
        this.howToRedeem = howToRedeem;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getCategory() {
        return category;
    }

    public String getBrandImg() {
        return brandImg;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public String getHowToRedeem() {
        return howToRedeem;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public void setHowToRedeem(String howToRedeem) {
        this.howToRedeem = howToRedeem;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}