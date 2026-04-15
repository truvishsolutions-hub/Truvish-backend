package com.truvish.truvishbackend.redemption;

import com.truvish.truvishbackend.TruvishCode.TruvishCode;
import com.truvish.truvishbackend.TruvishCode.TruvishCodeRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRedemptionService {

    private final UserRedemptionRepository repo;
    private final TruvishCodeRepository truvishCodeRepository;

    public UserRedemptionService(
            UserRedemptionRepository repo,
            TruvishCodeRepository truvishCodeRepository
    ) {
        this.repo = repo;
        this.truvishCodeRepository = truvishCodeRepository;
    }

    public List<UserRedemption> byPhone(String phone) {
        return repo.findByUserPhoneNumber(phone);
    }

    public List<RedemptionHistoryItemResponse> getHistory(String phone, String code) {
        String normalizedPhone = normalize(phone);
        String normalizedCode = normalize(code);

        if (isBlank(normalizedPhone) && isBlank(normalizedCode)) {
            return Collections.emptyList();
        }

        List<UserRedemption> rows;

        if (!isBlank(normalizedPhone) && !isBlank(normalizedCode)) {
            rows = repo.findByUserPhoneNumberOrUserTruvishCodeOrderByUserBrandTimeTempDesc(
                    normalizedPhone,
                    normalizedCode
            );
        } else if (!isBlank(normalizedPhone)) {
            rows = repo.findByUserPhoneNumberOrderByUserBrandTimeTempDesc(normalizedPhone);
        } else {
            rows = repo.findByUserTruvishCodeOrderByUserBrandTimeTempDesc(normalizedCode);
        }

        Map<Long, RedemptionHistoryItemResponse> uniqueMap = new LinkedHashMap<>();

        for (UserRedemption item : rows) {
            String brandLogo = item.getBrandLogo();

            if ((brandLogo == null || brandLogo.isBlank())) {
                Optional<TruvishCode> codeOpt =
                        truvishCodeRepository.findByTruvishIdCodeNumber(item.getUserTruvishCode());

                if (codeOpt.isPresent()) {
                    TruvishCode codeEntity = codeOpt.get();
                    brandLogo = codeEntity.getClientImg();
                }
            }

            RedemptionHistoryItemResponse response = new RedemptionHistoryItemResponse(
                    item.getUserId(),
                    item.getClientId(),
                    item.getClientCompanyName(),
                    item.getUserPhoneNumber(),
                    item.getUserTruvishCode(),
                    item.getUserBrandName(),
                    item.getUserBrandValue(),
                    item.getUserBrandVoucher(),
                    item.getUserBrandPin(),
                    item.getUserBrandValidity(),
                    item.getUserBrandTimeTemp(),
                    item.getBeforeBalance(),
                    item.getAfterBalance(),
                    item.getHistoryMessage(),
                    item.getRedeemStatus(),
                    brandLogo,
                    item.getRedemptionProcess()
            );

            uniqueMap.put(item.getUserId(), response);
        }

        return uniqueMap.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                RedemptionHistoryItemResponse::getUserBrandTimeTemp,
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                )
                .collect(Collectors.toList());
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}