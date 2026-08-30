package com.truvish.truvishbackend.redemption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRedemptionRepository
        extends JpaRepository<UserRedemption, Long> {

    // =========================================================
    // PHONE
    // =========================================================

    List<UserRedemption> findByUserPhoneNumber(
            String userPhoneNumber
    );


    // =========================================================
    // PHONE - NEWEST FIRST
    // =========================================================

    List<UserRedemption>
    findByUserPhoneNumberOrderByUserBrandTimeTempDesc(
            String userPhoneNumber
    );


    // =========================================================
    // CODE - NEWEST FIRST
    // =========================================================

    List<UserRedemption>
    findByUserTruvishCodeOrderByUserBrandTimeTempDesc(
            String userTruvishCode
    );


    // =========================================================
    // PHONE OR CODE - NEWEST FIRST
    // =========================================================

    List<UserRedemption>
    findByUserPhoneNumberOrUserTruvishCodeOrderByUserBrandTimeTempDesc(
            String userPhoneNumber,
            String userTruvishCode
    );


    // =========================================================
    // CODE - OLDEST FIRST
    //
    // Used by TruvishCodeService history.
    // =========================================================

    List<UserRedemption>
    findByUserTruvishCodeOrderByUserBrandTimeTempAsc(
            String userTruvishCode
    );


    // =========================================================
    // CLIENT ID - NEWEST FIRST
    // =========================================================

    List<UserRedemption>
    findByClientIdOrderByUserBrandTimeTempDesc(
            Long clientId
    );


    // =========================================================
    // COUNT BY CLIENT
    //
    // Used by ClientService.
    //
    // Counts redemption rows belonging to client.
    // =========================================================

    long countByClientId(
            Long clientId
    );


    // =========================================================
    // COUNT DISTINCT USERS
    //
    // Used by ClientService dashboard.
    //
    // userId ko unique user maana gaya hai.
    // =========================================================

    @Query("""
            SELECT COUNT(DISTINCT u.userId)
            FROM UserRedemption u
            WHERE u.userId IS NOT NULL
            """)
    Long countDistinctUsers();


    // =========================================================
    // COUNT DISTINCT USERS BY CLIENT
    //
    // Used by CorporateDashboardService.
    // =========================================================

    @Query("""
            SELECT COUNT(DISTINCT u.userId)
            FROM UserRedemption u
            WHERE u.clientId = :clientId
              AND u.userId IS NOT NULL
            """)
    Long countDistinctUsersByClientId(
            @Param("clientId") Long clientId
    );


    // =========================================================
    // COUNT DISTINCT REDEEMED CODES BY CLIENT
    //
    // Used by CorporateDashboardService.
    //
    // Same code multiple times redeem/history me aaye,
    // to ek hi code count hoga.
    // =========================================================

    @Query("""
            SELECT COUNT(DISTINCT u.userTruvishCode)
            FROM UserRedemption u
            WHERE u.clientId = :clientId
              AND u.userTruvishCode IS NOT NULL
            """)
    Long countDistinctRedeemedCodesByClientId(
            @Param("clientId") Long clientId
    );


    // =========================================================
    // REDEEMED AMOUNT BY CLIENT
    //
    // Used by ClientService + CorporateDashboardService.
    // =========================================================

    @Query("""
            SELECT COALESCE(SUM(u.userBrandValue), 0)
            FROM UserRedemption u
            WHERE u.clientId = :clientId
            """)
    Long sumRedeemedAmountByClientId(
            @Param("clientId") Long clientId
    );


    // =========================================================
    // TOTAL REDEEMED AMOUNT
    //
    // Optional helper for dashboard.
    // =========================================================

    @Query("""
            SELECT COALESCE(SUM(u.userBrandValue), 0)
            FROM UserRedemption u
            """)
    Long sumTotalRedeemedAmount();


    // =========================================================
    // OPTIONAL:
    // COUNT REDEMPTIONS BY CLIENT
    // =========================================================

    @Query("""
            SELECT COUNT(u)
            FROM UserRedemption u
            WHERE u.clientId = :clientId
            """)
    Long countRedemptionsByClientId(
            @Param("clientId") Long clientId
    );
}