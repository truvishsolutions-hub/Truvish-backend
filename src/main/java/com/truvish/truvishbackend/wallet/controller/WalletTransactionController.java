package com.truvish.truvishbackend.wallet.controller;

import com.truvish.truvishbackend.wallet.dto.CreateWalletTxnRequest;
import com.truvish.truvishbackend.wallet.dto.WalletTxnResponse;
import com.truvish.truvishbackend.wallet.service.WalletTransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletTransactionController {

    private final WalletTransactionService service;

    public WalletTransactionController(WalletTransactionService service) {
        this.service = service;
    }

    // ✅ WalletScreen history (DTO return => no Hibernate proxy error)
    @GetMapping("/{clientId}/transactions")
    public Page<WalletTxnResponse> transactions(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.latest(clientId, page, size)
                .map(t -> new WalletTxnResponse(
                        t.getTxnId(),
                        t.getTxnDateTime(),
                        t.getAmount(),
                        t.getType() == null ? null : t.getType().name(),
                        t.getDescription(),
                        t.getReferenceType(),
                        t.getReferenceId(),
                        t.getStatus() == null ? null : t.getStatus().name(),
                        t.getCreatedAt()
                ));
    }

    // ✅ Add transaction (DTO return => safe)
    @PostMapping("/{clientId}/transactions")
    public WalletTxnResponse create(
            @PathVariable Long clientId,
            @Valid @RequestBody CreateWalletTxnRequest req
    ) {
        var t = service.create(clientId, req);

        return new WalletTxnResponse(
                t.getTxnId(),
                t.getTxnDateTime(),
                t.getAmount(),
                t.getType() == null ? null : t.getType().name(),
                t.getDescription(),
                t.getReferenceType(),
                t.getReferenceId(),
                t.getStatus() == null ? null : t.getStatus().name(),
                t.getCreatedAt()
        );
    }
}