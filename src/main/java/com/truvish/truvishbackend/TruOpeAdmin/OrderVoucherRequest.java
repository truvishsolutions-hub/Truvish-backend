package com.truvish.truvishbackend.TruOpeAdmin;

import jakarta.validation.constraints.*;
import java.util.List;

public class OrderVoucherRequest {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotBlank(message = "Theme name is required")
    private String themeName;

    @NotBlank(message = "Theme image is required")
    private String themeImg;

    @NotNull(message = "Validity months is required")
    @Min(value = 1, message = "Validity must be at least 1 month")
    private Integer validityMonths;

    @NotEmpty(message = "At least one denomination item is required")
    private List<DenomItem> items;

    // ---- Getters & Setters ----
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public String getThemeName() { return themeName; }
    public void setThemeName(String themeName) { this.themeName = themeName; }

    public String getThemeImg() { return themeImg; }
    public void setThemeImg(String themeImg) { this.themeImg = themeImg; }

    public Integer getValidityMonths() { return validityMonths; }
    public void setValidityMonths(Integer validityMonths) { this.validityMonths = validityMonths; }

    public List<DenomItem> getItems() { return items; }
    public void setItems(List<DenomItem> items) { this.items = items; }

    public static class DenomItem {
        @NotNull
        @Positive
        private Long denomination;

        @NotNull
        @Positive
        private Integer quantity;

        public Long getDenomination() { return denomination; }
        public void setDenomination(Long denomination) { this.denomination = denomination; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}