package com.truvish.truvishbackend.corporateDashboard.CodesReport;

import com.truvish.truvishbackend.TruvishCode.TruvishCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodeReportRepository
        extends JpaRepository<TruvishCode, Long> {


    // =========================================================
    // GET ALL CODES OF ONE CLIENT
    //
    // Latest issued code will come first
    // =========================================================
    List<TruvishCode>
    findByClientIdOrderByTruvishCodeTimestampDesc(
            Long clientId
    );


    // =========================================================
    // GET SINGLE CODE
    // =========================================================
    Optional<TruvishCode>
    findByTruvishIdCodeNumber(
            String truvishIdCodeNumber
    );


    // =========================================================
    // GET CODES BY CLIENT + DATABASE STATUS
    //
    // ACTIVE / REDEEMED / EXPIRED
    // =========================================================
    List<TruvishCode>
    findByClientIdAndTruvishCodeStatusOrderByTruvishCodeTimestampDesc(
            Long clientId,
            com.truvish.truvishbackend.TruvishCode.VoucherStatus status
    );
}
