package com.truvish.truvishbackend.ClientChooseBrand.DTO;

public class ClientChooseBrandResponse {

    private Long id;
    private String brandName;
    private String category;
    private String brandImg;
    private String termsAndConditions;
    private String howToRedeem;

    public ClientChooseBrandResponse() {
    }

    public ClientChooseBrandResponse(
            Long id,
            String brandName,
            String category,
            String brandImg,
            String termsAndConditions,
            String howToRedeem
    ) {
        this.id = id;
        this.brandName = brandName;
        this.category = category;
        this.brandImg = brandImg;
        this.termsAndConditions = termsAndConditions;
        this.howToRedeem = howToRedeem;
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
}