package com.truvish.truvishbackend.client;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Version;

@Entity
@Table(name = "client_request",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "mobile_number"),
                @UniqueConstraint(columnNames = "email")
        })
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name="mobile_number", nullable = false, length = 15)
    private String mobileNumber;

    @Column(name="company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name="client_name", nullable = false, length = 150)
    private String clientName;

    @Column(name="email", nullable = false, length = 200)
    private String email;

    // ✅ CHANGED: logo_img (stores only filename)
    @Column(name="logo_img", length = 255)
    private String logoImg;

    @Column(name="balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal balance;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;



    @PrePersist
    public void prePersist() {
        if (balance == null) balance = new BigDecimal("1000.00");
        createdAt = LocalDateTime.now();
    }




    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }


    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // ✅ NEW getters/setters
    public String getLogoImg() { return logoImg; }
    public void setLogoImg(String logoImg) { this.logoImg = logoImg; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }


}
