package com.truvish.truvishbackend.TruvishCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TruvishCodeRepository extends JpaRepository<TruvishCode, Long> {

    Optional<TruvishCode> findByTruvishIdCodeNumber(String code);

    @Query(
            value = "SELECT * FROM truvish_code_generator " +
                    "WHERE client_name IS NULL " +
                    "AND client_img IS NULL " +
                    "AND truvish_code_value IS NULL " +
                    "AND client_theme IS NULL " +
                    "AND client_theme_img IS NULL " +
                    "AND client_brand IS NULL " +
                    "AND client_category IS NULL " +
                    "AND validity IS NULL " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<TruvishCode> findEmptyRow();

    List<TruvishCode> findByClientNameOrderByTruvishCodeTimestampDesc(String clientName);

    long countByClientId(Long clientId);

    @Query("select coalesce(sum(t.originalCodeValue), 0) from TruvishCode t where t.clientId = :clientId")
    Long sumDistributedValueByClientId(Long clientId);

    @Query("select count(t) from TruvishCode t where t.clientId is not null")
    Long countAllDistributedCodes();

    @Query("select coalesce(sum(t.originalCodeValue), 0) from TruvishCode t where t.clientId is not null")
    Long sumAllDistributedValue();
}