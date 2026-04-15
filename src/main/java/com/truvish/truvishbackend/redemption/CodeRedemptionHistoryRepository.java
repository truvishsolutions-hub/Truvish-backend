package com.truvish.truvishbackend.redemption;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRedemptionHistoryRepository extends JpaRepository<CodeRedemptionHistory, Long> {

    List<CodeRedemptionHistory> findByTruvishCodeOrderByRedeemedAtDesc(String truvishCode);

    List<CodeRedemptionHistory> findByPhoneNumberOrderByRedeemedAtDesc(String phoneNumber);
}