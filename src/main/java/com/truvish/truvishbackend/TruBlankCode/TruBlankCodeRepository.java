package com.truvish.truvishbackend.TruBlankCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TruBlankCodeRepository
        extends JpaRepository<TruBlankCode, Long> {


    // =========================================================
    // GET ALL
    // =========================================================

    Page<TruBlankCode> findAllByOrderByCreatedAtDesc(
            Pageable pageable
    );


    // =========================================================
    // GET BY CLIENT
    // =========================================================

    List<TruBlankCode> findByClientIdOrderByCreatedAtDesc(
            Long clientId
    );


    // =========================================================
    // GET BY CLIENT - PAGED
    // =========================================================

    Page<TruBlankCode> findByClientIdOrderByCreatedAtDesc(
            Long clientId,
            Pageable pageable
    );


    // =========================================================
    // GET BY ID
    // =========================================================

    Optional<TruBlankCode> findById(
            Long id
    );


    // =========================================================
    // GET BY SERIAL
    // =========================================================

    Optional<TruBlankCode> findBySerialNumber(
            String serialNumber
    );


    // =========================================================
    // GET BY REFERENCE
    // =========================================================

    Optional<TruBlankCode> findByReferenceNumber(
            String referenceNumber
    );


    // =========================================================
    // GET BY CODE
    // =========================================================

    Optional<TruBlankCode> findByCodeNumber(
            String codeNumber
    );


    // =========================================================
    // GENERATION NUMBER
    // =========================================================

    Optional<TruBlankCode>
    findTopByOrderByGenerationNumberDesc();


    // =========================================================
    // SERIAL NUMBER
    // =========================================================

    Optional<TruBlankCode>
    findTopByOrderBySerialNumberDesc();


    // =========================================================
    // UNIQUE CHECK - CODE
    // =========================================================

    boolean existsByCodeNumber(
            String codeNumber
    );


    // =========================================================
    // UNIQUE CHECK - REFERENCE
    // =========================================================

    boolean existsByReferenceNumber(
            String referenceNumber
    );


    // =========================================================
    // STATUS
    // =========================================================

    Page<TruBlankCode> findByStatusOrderByCreatedAtDesc(
            TruBlankCodeStatus status,
            Pageable pageable
    );


    // =========================================================
    // GLOBAL COUNT
    // =========================================================

    long countByStatus(
            TruBlankCodeStatus status
    );


    // =========================================================
    // CLIENT COUNT
    // =========================================================

    long countByClientId(
            Long clientId
    );


    // =========================================================
    // CLIENT + STATUS COUNT
    //
    // IMPORTANT:
    // Keep this method ONLY ONCE.
    // =========================================================

    long countByClientIdAndStatus(
            Long clientId,
            TruBlankCodeStatus status
    );


    // =========================================================
    // CLIENT ACTIVE COUNT
    //
    // ACTIVE + expiryDate > now
    // =========================================================

    @Query("""
        select count(t)
        from TruBlankCode t
        where t.clientId = :clientId
          and t.status = :activeStatus
          and (
                t.expiryDate is null
                or t.expiryDate > :now
          )
    """)
    long countClientActiveCodes(
            @Param("clientId") Long clientId,
            @Param("activeStatus") TruBlankCodeStatus activeStatus,
            @Param("now") LocalDateTime now
    );


    // =========================================================
    // CLIENT EXPIRED COUNT
    //
    // Already EXPIRED
    // OR ACTIVE but expiry date reached
    // =========================================================

    @Query("""
        select count(t)
        from TruBlankCode t
        where t.clientId = :clientId
          and (
                t.status = :expiredStatus
                or (
                    t.status = :activeStatus
                    and t.expiryDate is not null
                    and t.expiryDate <= :now
                )
          )
    """)
    long countClientExpiredCodes(
            @Param("clientId") Long clientId,
            @Param("expiredStatus") TruBlankCodeStatus expiredStatus,
            @Param("activeStatus") TruBlankCodeStatus activeStatus,
            @Param("now") LocalDateTime now
    );


    // =========================================================
    // GLOBAL EXPIRED ACTIVE CODES
    //
    // Used by refreshExpiredCodes()
    // =========================================================

    @Query("""
        select t
        from TruBlankCode t
        where t.status = :activeStatus
          and t.expiryDate is not null
          and t.expiryDate <= :now
    """)
    List<TruBlankCode> findExpiredActiveCodes(
            @Param("activeStatus") TruBlankCodeStatus activeStatus,
            @Param("now") LocalDateTime now
    );


    // =========================================================
    // CLIENT - TOTAL DENOMINATION
    // =========================================================

    @Query("""
        select coalesce(
            sum(t.denomination),
            0
        )
        from TruBlankCode t
        where t.clientId = :clientId
    """)
    BigDecimal sumDenominationByClientId(
            @Param("clientId") Long clientId
    );


    // =========================================================
    // CLIENT - STATUS DENOMINATION
    // =========================================================

    @Query("""
        select coalesce(
            sum(t.denomination),
            0
        )
        from TruBlankCode t
        where t.clientId = :clientId
          and t.status = :status
    """)
    BigDecimal sumDenominationByClientIdAndStatus(
            @Param("clientId") Long clientId,
            @Param("status") TruBlankCodeStatus status
    );


    // =========================================================
    // CLIENT - ACTIVE VALUE
    //
    // ACTIVE + not expired
    // =========================================================

    @Query("""
        select coalesce(
            sum(t.denomination),
            0
        )
        from TruBlankCode t
        where t.clientId = :clientId
          and t.status = :activeStatus
          and (
                t.expiryDate is null
                or t.expiryDate > :now
          )
    """)
    BigDecimal sumClientActiveValue(
            @Param("clientId") Long clientId,
            @Param("activeStatus") TruBlankCodeStatus activeStatus,
            @Param("now") LocalDateTime now
    );


    // =========================================================
    // CLIENT - EXPIRED VALUE
    //
    // EXPIRED
    // OR ACTIVE but expiry date reached
    // =========================================================

    @Query("""
        select coalesce(
            sum(t.denomination),
            0
        )
        from TruBlankCode t
        where t.clientId = :clientId
          and (
                t.status = :expiredStatus
                or (
                    t.status = :activeStatus
                    and t.expiryDate is not null
                    and t.expiryDate <= :now
                )
          )
    """)
    BigDecimal sumClientExpiredValue(
            @Param("clientId") Long clientId,
            @Param("expiredStatus") TruBlankCodeStatus expiredStatus,
            @Param("activeStatus") TruBlankCodeStatus activeStatus,
            @Param("now") LocalDateTime now
    );
}