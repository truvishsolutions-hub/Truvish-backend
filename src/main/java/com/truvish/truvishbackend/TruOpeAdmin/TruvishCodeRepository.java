package com.truvish.truvishbackend.TruOpeAdmin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TruvishCodeRepository
        extends JpaRepository<TruvishCode, Long> {

    // =========================================================
    // FIND CODE
    // =========================================================

    Optional<TruvishCode> findByTruvishIdCodeNumber(
            String code
    );


    // =========================================================
    // CHECK DUPLICATE
    // =========================================================

    boolean existsByTruvishIdCodeNumber(
            String code
    );


    // =========================================================
    // CLIENT HISTORY
    // =========================================================

    Page<TruvishCode>
    findByClientNameOrderByTruvishCodeTimestampDesc(
            String clientName,
            Pageable pageable
    );


    List<TruvishCode>
    findByClientNameIgnoreCaseOrderByTruvishCodeTimestampDesc(
            String clientName
    );


    // =========================================================
    // CODE REPORT
    // =========================================================

    List<TruvishCode>
    findByClientIdOrderByTruvishCodeTimestampDesc(
            Long clientId
    );


    // =========================================================
    // EMPTY ROW
    // =========================================================

    @Query(
            value = """
                    SELECT *
                    FROM truvish_code_generator
                    WHERE client_name IS NULL
                      AND client_img IS NULL
                      AND truvish_code_value IS NULL
                      AND client_theme IS NULL
                      AND client_theme_img IS NULL
                      AND client_brand IS NULL
                      AND client_category IS NULL
                      AND validity IS NULL
                    LIMIT 1
                    """,
            nativeQuery = true
    )
    Optional<TruvishCode> findEmptyRow();


    // =========================================================
    // TOTAL ISSUED CODES BY CLIENT
    // =========================================================

    long countByClientId(
            Long clientId
    );


    // =========================================================
    // TOTAL ISSUED VALUE BY CLIENT
    //
    // Used by Client Overview.
    // Response expects Long.
    // =========================================================

    @Query("""
            select coalesce(
                sum(t.originalCodeValue),
                0
            )
            from TruvishCode t
            where t.clientId = :clientId
            """)
    Long sumDistributedValueByClientId(
            @Param("clientId")
            Long clientId
    );


    // =========================================================
    // TOTAL DIGITAL VALUE BY CLIENT
    //
    // Used by Corporate Dashboard.
    // Response expects BigDecimal.
    // =========================================================

    @Query("""
            select coalesce(
                sum(t.originalCodeValue),
                0
            )
            from TruvishCode t
            where t.clientId = :clientId
            """)
    BigDecimal sumTruvishCodeValueByClientId(
            @Param("clientId")
            Long clientId
    );


    // =========================================================
    // COUNT BY CLIENT + STATUS
    // =========================================================

    long countByClientIdAndTruvishCodeStatus(
            Long clientId,
            VoucherStatus status
    );


    // =========================================================
    // VALUE BY CLIENT + STATUS
    // =========================================================

    @Query("""
            select coalesce(
                sum(t.originalCodeValue),
                0
            )
            from TruvishCode t
            where t.clientId = :clientId
              and t.truvishCodeStatus = :status
            """)
    BigDecimal sumTruvishCodeValueByClientIdAndStatus(
            @Param("clientId")
            Long clientId,

            @Param("status")
            VoucherStatus status
    );


    // =========================================================
    // REDEEMED VALUE BY CLIENT + STATUS
    // =========================================================

    @Query("""
            select coalesce(
                sum(t.originalCodeValue),
                0
            )
            from TruvishCode t
            where t.clientId = :clientId
              and t.truvishCodeStatus = :status
            """)
    BigDecimal sumOriginalCodeValueByClientIdAndStatus(
            @Param("clientId")
            Long clientId,

            @Param("status")
            VoucherStatus status
    );


    // =========================================================
    // ACTIVE COUNT
    // =========================================================

    @Query("""
            select count(t)
            from TruvishCode t
            where t.clientId = :clientId
              and t.truvishCodeStatus = :status
            """)
    Long countActiveByClientIdAndStatus(
            @Param("clientId")
            Long clientId,

            @Param("status")
            VoucherStatus status
    );


    // =========================================================
    // DISTRIBUTED CODES BY CLIENT
    // =========================================================

    @Query("""
            select count(t)
            from TruvishCode t
            where t.clientId = :clientId
            """)
    Long countDistributedCodesByClientId(
            @Param("clientId")
            Long clientId
    );


    // =========================================================
    // TOTAL DIGITAL CODES - ALL CLIENTS
    //
    // Used by Admin Dashboard.
    // =========================================================

    @Query("""
            select count(t)
            from TruvishCode t
            where t.clientId is not null
            """)
    Long countAllDistributedCodes();


    // =========================================================
    // TOTAL DIGITAL DISTRIBUTED VALUE - ALL CLIENTS
    //
    // DashboardSummaryResponse expects Long.
    // =========================================================

    @Query("""
            select coalesce(
                sum(t.originalCodeValue),
                0
            )
            from TruvishCode t
            where t.clientId is not null
            """)
    Long sumAllDistributedValue();
}