package com.truvish.truvishbackend.TruCard.controller;

import com.truvish.truvishbackend.TruCard.dto.TruCardCodeRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardCodeResponse;
import com.truvish.truvishbackend.TruCard.enums.TruCardCodeStatus;
import com.truvish.truvishbackend.TruCard.service.TruCardCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucard/codes")
//@CrossOrigin(origins = "*")
public class TruCardCodeController {

    private final TruCardCodeService truCardCodeService;

    public TruCardCodeController(
            TruCardCodeService truCardCodeService
    ) {
        this.truCardCodeService = truCardCodeService;
    }

    // =========================================================
    // GET ALL TRUCARD CODES
    // =========================================================

    @GetMapping
    public ResponseEntity<List<TruCardCodeResponse>> getAllCodes() {

        return ResponseEntity.ok(
                truCardCodeService.getAllCodes()
        );
    }

    // =========================================================
    // GET CODE BY DATABASE ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<TruCardCodeResponse> getCodeById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getCodeById(id)
        );
    }

    // =========================================================
    // GET CLIENT CODES
    // =========================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TruCardCodeResponse>> getCodesByClient(
            @PathVariable Long clientId
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getCodesByClient(clientId)
        );
    }

    // =========================================================
    // GET ORDER CODES
    // =========================================================

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<TruCardCodeResponse>> getCodesByOrder(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getOrderCodes(orderId)
        );
    }

    // =========================================================
    // GET CAMPAIGN CODES
    // =========================================================

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<TruCardCodeResponse>> getCodesByCampaign(
            @PathVariable Long campaignId
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getCampaignCodes(campaignId)
        );
    }

    // =========================================================
    // SEARCH BY SERIAL NUMBER
    // =========================================================

    @GetMapping("/search/serial/{serialNumber}")
    public ResponseEntity<TruCardCodeResponse> searchBySerialNumber(
            @PathVariable String serialNumber
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getBySerialNumber(
                        serialNumber
                )
        );
    }

    // =========================================================
    // SEARCH BY REFERENCE NUMBER
    // =========================================================

    @GetMapping("/search/reference/{referenceNumber}")
    public ResponseEntity<TruCardCodeResponse> searchByReferenceNumber(
            @PathVariable String referenceNumber
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getByReferenceNumber(
                        referenceNumber
                )
        );
    }

    // =========================================================
    // SEARCH BY SERIAL / REFERENCE / CODE
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<List<TruCardCodeResponse>> searchCodes(
            @RequestParam String keyword
    ) {

        return ResponseEntity.ok(
                truCardCodeService.searchCodes(keyword)
        );
    }

    // =========================================================
    // GET CODES BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TruCardCodeResponse>> getCodesByStatus(
            @PathVariable TruCardCodeStatus status
    ) {

        return ResponseEntity.ok(
                truCardCodeService.getAllCodesByStatus(status)
        );
    }

    // =========================================================
    // GENERATE TRUCARD CODES
    // =========================================================
    //
    // quantity = 3
    // => 3 different TruCard codes generate honge.
    //
    // Example:
    //
    // {
    //     "clientId": 19,
    //     "quantity": 3,
    //     "denomination": 100,
    //     "validityMonths": 12
    // }
    //
    // =========================================================

    @PostMapping("/generate")
    public ResponseEntity<List<TruCardCodeResponse>> generateCodes(
            @RequestBody TruCardCodeRequest request
    ) {

        List<TruCardCodeResponse> responses =
                truCardCodeService.generateCodes(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responses);
    }

    // =========================================================
    // ACTIVATE CODE
    // =========================================================

    @PutMapping("/{id}/activate")
    public ResponseEntity<TruCardCodeResponse> activateCode(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                truCardCodeService.activateCode(id)
        );
    }

    // =========================================================
    // DEACTIVATE CODE
    // =========================================================

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<TruCardCodeResponse> deactivateCode(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                truCardCodeService.deactivateCode(id)
        );
    }

    // =========================================================
    // UPDATE VALIDITY
    // =========================================================

    @PutMapping("/{id}/validity")
    public ResponseEntity<TruCardCodeResponse> updateValidity(
            @PathVariable Long id,
            @RequestParam Integer validityMonths
    ) {

        return ResponseEntity.ok(
                truCardCodeService.updateValidity(
                        id,
                        validityMonths
                )
        );
    }

    // =========================================================
    // REDEEM CODE
    // =========================================================

    @PutMapping("/{id}/redeem")
    public ResponseEntity<TruCardCodeResponse> redeemCode(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                truCardCodeService.redeemCode(id)
        );
    }

    // =========================================================
    // EXPIRE CODE
    // =========================================================

    @PutMapping("/{id}/expire")
    public ResponseEntity<TruCardCodeResponse> expireCode(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                truCardCodeService.expireCode(id)
        );
    }

    // =========================================================
    // DELETE CODE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(
            @PathVariable Long id
    ) {

        truCardCodeService.deleteCode(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}