package com.truvish.truvishbackend.corporateDashboard.CodesReport;

public enum CodeReportStatus {

    REDEEMED("Redeemed"),

    ACTIVE("Active"),

    EXPIRED_BACK_TO_WALLET("Expired-Back to wallet");

    private final String label;

    CodeReportStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}