package com.truvish.truvishbackend.redemption;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeRedemptionHistoryService {

    private final CodeRedemptionHistoryRepository repository;

    public CodeRedemptionHistoryService(CodeRedemptionHistoryRepository repository) {
        this.repository = repository;
    }

    public List<CodeRedemptionHistoryResponse> byCode(String truvishCode) {
        return repository.findByTruvishCodeOrderByRedeemedAtDesc(truvishCode)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CodeRedemptionHistoryResponse> byPhone(String phoneNumber) {
        return repository.findByPhoneNumberOrderByRedeemedAtDesc(phoneNumber)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CodeRedemptionHistoryResponse mapToResponse(CodeRedemptionHistory row) {
        return new CodeRedemptionHistoryResponse(
                row.getId(),
                row.getClientId(),
                row.getClientCompanyName(),
                row.getTruvishCode(),
                row.getPhoneNumber(),
                row.getBrandName(),
                row.getRedeemedValue(),
                row.getVoucherCode(),
                row.getVoucherPin(),
                row.getValidityTill(),
                row.getBeforeBalance(),
                row.getAfterBalance(),
                row.getRedeemStatus(),
                row.getHistoryMessage(),
                row.getBrandLogo(),
                row.getRedemptionProcess(),
                row.getRedeemedAt(),
                row.getCreatedAt()
        );
    }
}