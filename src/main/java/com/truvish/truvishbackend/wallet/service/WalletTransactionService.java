package com.truvish.truvishbackend.wallet.service;

import com.truvish.truvishbackend.client.Client;
import com.truvish.truvishbackend.client.ClientRepository;
import com.truvish.truvishbackend.wallet.WalletTransaction;
import com.truvish.truvishbackend.wallet.WalletTransactionRepository;
import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.TxnType;
import com.truvish.truvishbackend.wallet.TxnStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
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

    // =========================================================
    // GET LATEST TRANSACTIONS
    // =========================================================

    @Transactional(readOnly = true)
    public Page<WalletTransaction> latest(
            Long clientId,
            int page,
            int size
    ) {

        if (clientId == null) {
            throw new IllegalArgumentException(
                    "Client ID is required"
            );
        }

        if (page < 0) {
            page = 0;
        }

        if (size < 1) {
            size = 20;
        }

        return txnRepo.findByClient_IdOrderByTxnDateTimeDesc(
                clientId,
                PageRequest.of(page, size)
        );
    }

    // =========================================================
    // CREATE WALLET TRANSACTION
    //
    // DIGITAL:
    // TYPE        = DEBIT
    // DESCRIPTION = Debited
    // REFERENCE   = VOUCHER
    //
    // PHYSICAL:
    // TYPE        = DEBIT
    // DESCRIPTION = TruCard Debited
    // REFERENCE   = TRUCARD
    //
    // IMPORTANT:
    // Physical TruCard ke case mein incoming description ko
    // ignore karke EXACT "TruCard Debited" save kiya jayega.
    // =========================================================

    public WalletTransaction create(
            Long clientId,
            CreateWalletTxnRequest req
    ) {

        if (clientId == null) {
            throw new IllegalArgumentException(
                    "Client ID is required"
            );
        }

        if (req == null) {
            throw new IllegalArgumentException(
                    "Wallet transaction request is required"
            );
        }

        Client client =
                clientRepo.findById(clientId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Client not found: " + clientId
                                )
                        );

        // =====================================================
        // TYPE
        // =====================================================

        if (
                req.getType() == null ||
                        req.getType().trim().isEmpty()
        ) {
            throw new IllegalArgumentException(
                    "Type is required"
            );
        }

        TxnType type;

        try {

            type = TxnType.valueOf(
                    req.getType()
                            .trim()
                            .toUpperCase()
            );

        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException(
                    "Type must be CREDIT or DEBIT"
            );
        }

        // =====================================================
        // AMOUNT
        // =====================================================

        BigDecimal amount =
                req.getAmount();

        if (amount == null) {

            throw new IllegalArgumentException(
                    "Amount is required"
            );
        }

        amount =
                amount
                        .abs()
                        .setScale(2);

        if (
                amount.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Amount must be greater than 0"
            );
        }

        // =====================================================
        // CURRENT BALANCE
        // =====================================================

        BigDecimal currentBalance =
                client.getBalance() == null
                        ? BigDecimal.ZERO.setScale(2)
                        : client
                        .getBalance()
                        .setScale(2);

        BigDecimal updatedBalance;

        // =====================================================
        // CREDIT
        // =====================================================

        if (
                type == TxnType.CREDIT
        ) {

            updatedBalance =
                    currentBalance.add(
                            amount
                    );

        }

        // =====================================================
        // DEBIT
        // =====================================================

        else {

            if (
                    currentBalance.compareTo(
                            amount
                    ) < 0
            ) {

                throw new IllegalArgumentException(
                        "Insufficient wallet balance. Available balance: "
                                + currentBalance
                );
            }

            updatedBalance =
                    currentBalance.subtract(
                            amount
                    );

            // Wallet history mein debit negative.
            amount =
                    amount.negate();
        }

        // =====================================================
        // UPDATE CLIENT BALANCE
        // =====================================================

        client.setBalance(
                updatedBalance.setScale(2)
        );

        clientRepo.save(
                client
        );

        // =====================================================
        // SAVE TRANSACTION
        // =====================================================

        WalletTransaction tx =
                new WalletTransaction();

        tx.setClient(
                client
        );

        tx.setType(
                type
        );

        tx.setAmount(
                amount
        );

        // =====================================================
        // DESCRIPTION
        //
        // PHYSICAL TRUCARD:
        //
        // referenceType = TRUCARD
        // type          = DEBIT
        //
        // ALWAYS:
        // "TruCard Debited"
        //
        // Kisi bhi old/generated description ko use nahi
        // kiya jayega.
        // =====================================================

        if (
                type == TxnType.DEBIT &&
                        req.getReferenceType() != null &&
                        "TRUCARD".equalsIgnoreCase(
                                req.getReferenceType().trim()
                        )
        ) {

            tx.setDescription(
                    "TruCard Debited"
            );

        } else if (
                type == TxnType.DEBIT &&
                        req.getReferenceType() != null &&
                        "VOUCHER".equalsIgnoreCase(
                                req.getReferenceType().trim()
                        )
        ) {

            tx.setDescription(
                    "Debited"
            );

        } else {

            String description =
                    req.getDescription();

            if (
                    description == null ||
                            description.trim().isEmpty()
            ) {

                description =
                        type == TxnType.DEBIT
                                ? "Debited"
                                : "Credited";
            }

            tx.setDescription(
                    description
            );
        }

        // =====================================================
        // REFERENCE TYPE
        // =====================================================

        tx.setReferenceType(
                req.getReferenceType()
        );

        // =====================================================
        // REFERENCE ID
        // =====================================================

        tx.setReferenceId(
                req.getReferenceId()
        );

        // =====================================================
        // STATUS
        // =====================================================

        tx.setStatus(
                TxnStatus.SUCCESS
        );

        // =====================================================
        // SAVE
        // =====================================================

        return txnRepo.save(
                tx
        );
    }

    // =========================================================
    // RECORD DEBIT ONLY
    //
    // Balance already updated by another service.
    // This method ONLY creates wallet history.
    // =========================================================

    public WalletTransaction recordDebit(
            Long clientId,
            BigDecimal amount,
            String description,
            String referenceType,
            String referenceId
    ) {

        if (clientId == null) {

            throw new IllegalArgumentException(
                    "Client ID is required"
            );
        }

        if (
                amount == null ||
                        amount.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Debit amount must be greater than 0"
            );
        }

        Client client =
                clientRepo.findById(
                                clientId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Client not found: "
                                                + clientId
                                )
                        );

        BigDecimal debitAmount =
                amount
                        .abs()
                        .setScale(2);

        // =====================================================
        // PHYSICAL TRUCARD
        //
        // EXACT DESCRIPTION
        // =====================================================

        String finalDescription;

        if (
                referenceType != null &&
                        "TRUCARD".equalsIgnoreCase(
                                referenceType.trim()
                        )
        ) {

            finalDescription =
                    "TruCard Debited";

        } else {

            finalDescription =
                    description;

            if (
                    finalDescription == null ||
                            finalDescription.trim().isEmpty()
            ) {

                finalDescription =
                        "Debited";
            }
        }

        // =====================================================
        // CREATE HISTORY
        // =====================================================

        WalletTransaction tx =
                new WalletTransaction();

        tx.setClient(
                client
        );

        tx.setAmount(
                debitAmount.negate()
        );

        tx.setType(
                TxnType.DEBIT
        );

        tx.setDescription(
                finalDescription
        );

        tx.setReferenceType(
                referenceType
        );

        tx.setReferenceId(
                referenceId
        );

        tx.setStatus(
                TxnStatus.SUCCESS
        );

        return txnRepo.save(
                tx
        );
    }

    // =========================================================
    // RECORD CREDIT ONLY
    // =========================================================

    public WalletTransaction recordCredit(
            Long clientId,
            BigDecimal amount,
            String description,
            String referenceType,
            String referenceId
    ) {

        if (clientId == null) {

            throw new IllegalArgumentException(
                    "Client ID is required"
            );
        }

        if (
                amount == null ||
                        amount.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
        ) {

            throw new IllegalArgumentException(
                    "Credit amount must be greater than 0"
            );
        }

        Client client =
                clientRepo.findById(
                                clientId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Client not found: "
                                                + clientId
                                )
                        );

        String finalDescription =
                description;

        if (
                finalDescription == null ||
                        finalDescription.trim().isEmpty()
        ) {

            finalDescription =
                    "Credited";
        }

        WalletTransaction tx =
                new WalletTransaction();

        tx.setClient(
                client
        );

        tx.setAmount(
                amount
                        .abs()
                        .setScale(2)
        );

        tx.setType(
                TxnType.CREDIT
        );

        tx.setDescription(
                finalDescription
        );

        tx.setReferenceType(
                referenceType
        );

        tx.setReferenceId(
                referenceId
        );

        tx.setStatus(
                TxnStatus.SUCCESS
        );

        return txnRepo.save(
                tx
        );
    }

    // =========================================================
    // CHECK EXISTING TRANSACTION
    // =========================================================

    @Transactional(readOnly = true)
    public boolean existsByReference(
            String referenceType,
            String referenceId
    ) {

        if (
                referenceType == null ||
                        referenceId == null
        ) {

            return false;
        }

        return txnRepo.existsByReferenceTypeAndReferenceId(
                referenceType,
                referenceId
        );
    }
}