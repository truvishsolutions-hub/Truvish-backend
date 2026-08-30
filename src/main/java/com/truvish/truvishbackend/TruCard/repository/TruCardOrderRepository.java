package com.truvish.truvishbackend.TruCard.repository;

import com.truvish.truvishbackend.TruCard.entity.TruCardOrder;
import com.truvish.truvishbackend.TruCard.enums.TruCardOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TruCardOrderRepository
        extends JpaRepository<TruCardOrder, Long> {

    List<TruCardOrder> findAllByOrderByCreatedAtDesc();

    List<TruCardOrder> findByClientIdOrderByCreatedAtDesc(
            Long clientId
    );

    List<TruCardOrder> findByClientIdAndStatusOrderByCreatedAtDesc(
            Long clientId,
            TruCardOrderStatus status
    );

    List<TruCardOrder> findByCampaignIdOrderByCreatedAtDesc(
            Long campaignId
    );
}