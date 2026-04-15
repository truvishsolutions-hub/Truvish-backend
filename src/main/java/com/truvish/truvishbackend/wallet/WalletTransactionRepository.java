package com.truvish.truvishbackend.wallet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    Page<WalletTransaction> findByClient_IdOrderByTxnDateTimeDesc(Long clientId, Pageable pageable);

    @Query("""
        select coalesce(sum(w.amount), 0)
        from WalletTransaction w
        where w.client.id = :clientId
          and w.type = com.truvish.truvishbackend.wallet.TxnType.CREDIT
          and w.status = com.truvish.truvishbackend.wallet.TxnStatus.SUCCESS
    """)
    BigDecimal sumTotalLoadByClientId(Long clientId);

    @Query("""
        select coalesce(sum(w.amount), 0)
        from WalletTransaction w
        where w.type = com.truvish.truvishbackend.wallet.TxnType.CREDIT
          and w.status = com.truvish.truvishbackend.wallet.TxnStatus.SUCCESS
    """)
    BigDecimal sumAllLoadedValue();
}