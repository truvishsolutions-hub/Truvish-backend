package com.truvish.truvishbackend.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClientRepository
        extends JpaRepository<Client, Long> {

    // =========================================================
    // EXISTS BY MOBILE
    // =========================================================

    boolean existsByMobileNumber(
            String mobileNumber
    );


    // =========================================================
    // FIND BY MOBILE
    // =========================================================

    Optional<Client> findByMobileNumber(
            String mobileNumber
    );


    // =========================================================
    // SEARCH CLIENT
    // =========================================================
    //
    // Searches:
    //
    // 1. Company Name
    // 2. Client Name
    // 3. Mobile Number
    // 4. Email
    //
    // Case insensitive.
    //
    // =========================================================

    @Query("""
            SELECT c
            FROM Client c
            WHERE
                LOWER(c.companyName) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.clientName) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :value, '%'))
            ORDER BY c.createdAt DESC
            """)
    List<Client> searchClients(
            @Param("value") String value
    );


    // =========================================================
    // SEARCH CLIENT PAGINATED
    // =========================================================

    @Query("""
            SELECT c
            FROM Client c
            WHERE
                LOWER(c.companyName) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.clientName) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.mobileNumber) LIKE LOWER(CONCAT('%', :value, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :value, '%'))
            """)
    Page<Client> searchClients(
            @Param("value") String value,
            Pageable pageable
    );


    // =========================================================
    // SAFE WALLET DEDUCTION
    // =========================================================

    @Modifying
    @Query("""
            UPDATE Client c
            SET c.balance = c.balance - :amount
            WHERE c.id = :clientId
            AND c.balance >= :amount
            """)
    int deductBalance(
            @Param("clientId") Long clientId,
            @Param("amount") BigDecimal amount
    );
}