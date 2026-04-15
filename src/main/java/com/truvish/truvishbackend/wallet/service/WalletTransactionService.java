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

import java.math.BigDecimal;

@Service
public class WalletTransactionService {

    private final WalletTransactionRepository txnRepo;
    private final ClientRepository clientRepo;

    public WalletTransactionService(WalletTransactionRepository txnRepo, ClientRepository clientRepo) {
        this.txnRepo = txnRepo;
        this.clientRepo = clientRepo;
    }

    public Page<WalletTransaction> latest(Long clientId, int page, int size) {
        return txnRepo.findByClient_IdOrderByTxnDateTimeDesc(clientId, PageRequest.of(page, size));
    }

    @Transactional
    public WalletTransaction create(Long clientId, CreateWalletTxnRequest req) {

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found: " + clientId));

        // ✅ CREDIT / DEBIT
        TxnType type;
        try {
            type = TxnType.valueOf(req.getType()); // getter already returns uppercase
        } catch (Exception e) {
            throw new RuntimeException("Type must be CREDIT or DEBIT");
        }

        BigDecimal amt = req.getAmount();
        if (amt == null) throw new RuntimeException("amount is required");

        // ✅ sign normalize:
        // CREDIT => +amount
        // DEBIT  => -amount
        BigDecimal signed = (type == TxnType.DEBIT) ? amt.abs().negate() : amt.abs();

        // ✅ update client balance snapshot
        BigDecimal current = client.getBalance() == null ? BigDecimal.ZERO : client.getBalance();
        BigDecimal updated = current.add(signed);

        client.setBalance(updated);
        clientRepo.save(client);

        // ✅ save transaction
        WalletTransaction tx = new WalletTransaction();
        tx.setClient(client);
        tx.setType(type);
        tx.setAmount(signed);
        tx.setDescription(req.getDescription());
        tx.setReferenceType(req.getReferenceType());
        tx.setReferenceId(req.getReferenceId());

        return txnRepo.save(tx);
    }
}