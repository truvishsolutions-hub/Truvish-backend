package com.truvish.truvishbackend.TruCard.repository;

import com.truvish.truvishbackend.TruCard.entity.TruCardCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruCardCampaignRepository
        extends JpaRepository<TruCardCampaign, Long> {

    // =========================================================
    // GET ALL CAMPAIGNS OF CLIENT
    // =========================================================

    List<TruCardCampaign> findByClientIdOrderByCreatedAtDesc(
            Long clientId
    );


    // =========================================================
    // GET ACTIVE CAMPAIGNS OF CLIENT
    // =========================================================

    List<TruCardCampaign> findByClientIdAndActiveTrueOrderByCreatedAtDesc(
            Long clientId
    );


    // =========================================================
    // GET ALL ACTIVE CAMPAIGNS
    // =========================================================

    List<TruCardCampaign> findByActiveTrueOrderByCreatedAtDesc();


    // =========================================================
    // CHECK CAMPAIGN EXISTS FOR CLIENT
    // =========================================================

    boolean existsByClientIdAndCampaignName(
            Long clientId,
            String campaignName
    );
}