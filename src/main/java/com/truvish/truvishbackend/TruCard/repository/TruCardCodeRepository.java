package com.truvish.truvishbackend.TruCard.repository;

import com.truvish.truvishbackend.TruCard.entity.TruCardCode;
import com.truvish.truvishbackend.TruCard.enums.TruCardCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruCardCodeRepository
        extends JpaRepository<TruCardCode, Long> {

    // =========================================================
    // GET ALL CODES
    // =========================================================

    List<TruCardCode> findAllByOrderByCreatedAtDesc();


    // =========================================================
    // GET ALL CODES OF CLIENT
    // =========================================================

    List<TruCardCode>
    findByClientIdOrderByCreatedAtDesc(Long clientId);


    // =========================================================
    // GET CODES OF CLIENT BY STATUS
    // =========================================================

    List<TruCardCode>
    findByClientIdAndStatusOrderByCreatedAtDesc(
            Long clientId,
            TruCardCodeStatus status
    );


    // =========================================================
    // GET ALL CODES BY STATUS
    // =========================================================

    List<TruCardCode>
    findByStatusOrderByCreatedAtDesc(
            TruCardCodeStatus status
    );


    // =========================================================
    // GET CODES BY ORDER
    // =========================================================

    List<TruCardCode>
    findByOrderId(Long orderId);


    // =========================================================
    // GET CODES BY CAMPAIGN
    // =========================================================

    List<TruCardCode>
    findByCampaignId(Long campaignId);


    // =========================================================
    // FIND BY DATABASE ID
    // =========================================================

    Optional<TruCardCode>
    findById(Long id);


    // =========================================================
    // FIND BY REDEEM CODE
    // =========================================================

    Optional<TruCardCode>
    findByCodeNumber(String codeNumber);


    // =========================================================
    // CHECK REDEEM CODE
    // =========================================================

    boolean existsByCodeNumber(String codeNumber);


    // =========================================================
    // FIND BY SERIAL NUMBER
    // =========================================================

    Optional<TruCardCode>
    findBySerialNumber(String serialNumber);


    // =========================================================
    // CHECK SERIAL NUMBER
    // =========================================================

    boolean existsBySerialNumber(String serialNumber);


    // =========================================================
    // FIND BY REFERENCE NUMBER
    // =========================================================

    Optional<TruCardCode>
    findByReferenceNumber(String referenceNumber);


    // =========================================================
    // CHECK REFERENCE NUMBER
    // =========================================================

    boolean existsByReferenceNumber(String referenceNumber);


    // =========================================================
    // FIND BY ANY IDENTIFIER
    //
    // Code OR Serial OR Reference
    // =========================================================

    Optional<TruCardCode>
    findByCodeNumberOrSerialNumberOrReferenceNumber(
            String codeNumber,
            String serialNumber,
            String referenceNumber
    );


    // =========================================================
    // SEARCH
    //
    // Code / Serial / Reference
    // =========================================================

    List<TruCardCode>
    findByCodeNumberContainingIgnoreCaseOrSerialNumberContainingOrReferenceNumberContainingIgnoreCase(
            String codeNumber,
            String serialNumber,
            String referenceNumber
    );


    // =========================================================
    // GET LATEST SERIAL NUMBER
    // =========================================================

    Optional<TruCardCode>
    findTopByOrderBySerialNumberDesc();


    // =========================================================
    // COUNT
    //
    // count() already comes from JpaRepository
    // =========================================================


    // =========================================================
    // COUNT BY STATUS
    // =========================================================

    long countByStatus(
            TruCardCodeStatus status
    );


    // =========================================================
    // COUNT BY CLIENT
    // =========================================================

    long countByClientId(
            Long clientId
    );


    // =========================================================
    // COUNT CLIENT CODES BY STATUS
    // =========================================================

    long countByClientIdAndStatus(
            Long clientId,
            TruCardCodeStatus status
    );
}