package com.truvish.truvishbackend.TruvishCode;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "truvish_code_generator")
public class TruvishCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long truvishId;

    private Long clientId;

    private String truvishIdCodeNumber;
    private LocalDateTime truvishCodeTimestamp;
    private Boolean truvishIdIsPartialRedeemAllowed;
    private String truvishCodeStatus;

    private String clientName;

    @Column(name = "client_img")
    private String clientImg;

    private Long truvishCodeValue;

    private Long originalCodeValue;

    private Integer validity;

    private String clientTheme;

    @Column(name = "client_brand")
    private String[] clientBrand;

    @Column(name = "client_category")
    private String[] clientCategory;

    @Column(name = "client_theme_img")
    private String clientThemeImg;

    public Long getTruvishId() { return truvishId; }
    public void setTruvishId(Long truvishId) { this.truvishId = truvishId; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getTruvishIdCodeNumber() { return truvishIdCodeNumber; }
    public void setTruvishIdCodeNumber(String truvishIdCodeNumber) { this.truvishIdCodeNumber = truvishIdCodeNumber; }

    public LocalDateTime getTruvishCodeTimestamp() { return truvishCodeTimestamp; }
    public void setTruvishCodeTimestamp(LocalDateTime truvishCodeTimestamp) { this.truvishCodeTimestamp = truvishCodeTimestamp; }

    public Boolean getTruvishIdIsPartialRedeemAllowed() { return truvishIdIsPartialRedeemAllowed; }
    public void setTruvishIdIsPartialRedeemAllowed(Boolean truvishIdIsPartialRedeemAllowed) { this.truvishIdIsPartialRedeemAllowed = truvishIdIsPartialRedeemAllowed; }

    public String getTruvishCodeStatus() { return truvishCodeStatus; }
    public void setTruvishCodeStatus(String truvishCodeStatus) { this.truvishCodeStatus = truvishCodeStatus; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getClientImg() { return clientImg; }
    public void setClientImg(String clientImg) { this.clientImg = clientImg; }

    public Long getTruvishCodeValue() { return truvishCodeValue; }
    public void setTruvishCodeValue(Long truvishCodeValue) { this.truvishCodeValue = truvishCodeValue; }

    public Long getOriginalCodeValue() { return originalCodeValue; }
    public void setOriginalCodeValue(Long originalCodeValue) { this.originalCodeValue = originalCodeValue; }

    public Integer getValidity() { return validity; }
    public void setValidity(Integer validity) { this.validity = validity; }

    public String getClientTheme() { return clientTheme; }
    public void setClientTheme(String clientTheme) { this.clientTheme = clientTheme; }

    public String[] getClientBrand() { return clientBrand; }
    public void setClientBrand(String[] clientBrand) { this.clientBrand = clientBrand; }

    public String[] getClientCategory() { return clientCategory; }
    public void setClientCategory(String[] clientCategory) { this.clientCategory = clientCategory; }

    public String getClientThemeImg() { return clientThemeImg; }
    public void setClientThemeImg(String clientThemeImg) { this.clientThemeImg = clientThemeImg; }
}