package com.truvish.truvishbackend.voucherinventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoucherInventoryRepository extends JpaRepository<VoucherInventory, Long> {

    boolean existsByVoucherCodeIgnoreCase(String voucherCode);

    List<VoucherInventory> findByBrandNameIgnoreCaseOrderByDenominationAscValidityTillAscCreatedAtAsc(String brandName);

    List<VoucherInventory> findByBrandNameIgnoreCaseAndDenominationOrderByValidityTillAscCreatedAtAsc(
            String brandName,
            BigDecimal denomination
    );


    Optional<VoucherInventory> findFirstByBrandNameIgnoreCaseAndDenominationAndStatusIgnoreCaseAndValidityTillGreaterThanEqualOrderByValidityTillAscCreatedAtAsc(
            String brandName,
            BigDecimal denomination,
            String status,
            LocalDate today
    );
}