package com.truvish.truvishbackend.wallet.service;

import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import com.truvish.truvishbackend.wallet.TxnType;
import com.truvish.truvishbackend.wallet.WalletTransaction;
import com.truvish.truvishbackend.wallet.WalletTransactionRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truvish.truvishbackend.exception.ResourceNotFoundException;

import java.math.BigDecimal;

@Service
public class WalletTransactionService {

    private final WalletTransactionRepository txnRepo;
    private final ClientRepository clientRepo;

    public WalletTransactionService(
            WalletTransactionRepository txnRepo,
            ClientRepository clientRepo
    ) {
        this.txnRepo = txnRepo;
        this.clientRepo = clientRepo;
    }

    public Page<WalletTransaction> latest(Long clientId, int page, int size) {

        return txnRepo.findByClient_IdOrderByTxnDateTimeDesc(
                clientId,
                PageRequest.of(page, size)
        );
    }

    @Transactional
    public WalletTransaction create(
            Long clientId,
            CreateWalletTxnRequest req
    ) {

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Client not found: " + clientId
                        )
                );

        // =========================================
        // CREDIT / DEBIT TYPE
        // =========================================

        TxnType type;

        try {
            type = TxnType.valueOf(req.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Type must be CREDIT or DEBIT"
            );
        }

        // =========================================
        // AMOUNT
        // =========================================

        BigDecimal amt = req.getAmount();

        if (amt == null) {
            throw new IllegalArgumentException(
                    "Amount is required"
            );
        }

        amt = amt.abs();

        // =========================================
        // CURRENT BALANCE
        // =========================================

        BigDecimal current =
                client.getBalance() == null
                        ? BigDecimal.ZERO
                        : client.getBalance();

        BigDecimal updated;

        // =========================================
        // CREDIT
        // =========================================

        if (type == TxnType.CREDIT) {

            updated = current.add(amt);

        }

        // =========================================
        // DEBIT
        // =========================================

        else {

            if (current.compareTo(amt) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient wallet balance. Available balance: "
                                + current
                );
            }

            updated = current.subtract(amt);

            // Store debit as negative amount in history
            amt = amt.negate();
        }

        // =========================================
        // UPDATE CLIENT BALANCE
        // =========================================

        client.setBalance(updated);

        clientRepo.save(client);

        // =========================================
        // SAVE TRANSACTION
        // =========================================

        WalletTransaction tx = new WalletTransaction();

        tx.setClient(client);
        tx.setType(type);
        tx.setAmount(amt);
        tx.setDescription(req.getDescription());
        tx.setReferenceType(req.getReferenceType());
        tx.setReferenceId(req.getReferenceId());

        return txnRepo.save(tx);
    }
}