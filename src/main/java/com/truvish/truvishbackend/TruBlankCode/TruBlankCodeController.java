package com.truvish.truvishbackend.TruBlankCode;

import com.truvish.truvishbackend.TruBlankCode.dto.ActivateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.AssignClientRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.GenerateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.dto.UpdateBlankCodeRequest;
import com.truvish.truvishbackend.TruBlankCode.response.TruBlankCodeResponse;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tru-blank-code")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        },
        allowCredentials = "true"
)
public class TruBlankCodeController {

    private final TruBlankCodeService service;


    public TruBlankCodeController(
            TruBlankCodeService service
    ) {
        this.service = service;
    }


    // =========================================================
    // GENERATE
    // =========================================================

    @PostMapping("/generate")
    public ResponseEntity<List<TruBlankCodeResponse>> generateCodes(

            @Valid
            @RequestBody
            GenerateBlankCodeRequest request,

            @RequestParam("adminId")
            Long adminId

    ) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        service.generateCodes(
                                request,
                                adminId
                        )
                );
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @GetMapping
    public ResponseEntity<Page<TruBlankCodeResponse>> getAllCodes(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                service.getAllCodes(
                        pageable
                )
        );
    }


    // =========================================================
    // GET ALL CODES BY CLIENT
    //
    // USED BY:
    // TruCard Code Report
    //
    // URL:
    // GET /api/admin/tru-blank-code/client/{clientId}
    // =========================================================

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<TruBlankCodeResponse>> getCodesByClient(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.getCodesByClient(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT SUMMARY
    //
    // USED BY TRUCARD DASHBOARD
    // =========================================================

    @GetMapping("/client/{clientId}/summary")
    public ResponseEntity<ClientTruCardSummary> getClientSummary(

            @PathVariable
            Long clientId

    ) {

        // -----------------------------------------------------
        // COUNTS
        // -----------------------------------------------------

        long issuedCodes =
                service.countClientCodes(
                        clientId
                );


        long inactiveCodes =
                service.countClientInactive(
                        clientId
                );


        long activeCodes =
                service.countClientActive(
                        clientId
                );


        long redeemedCodes =
                service.countClientRedeemed(
                        clientId
                );


        long expiredCodes =
                service.countClientExpired(
                        clientId
                );


        /*
         * IMPORTANT:
         *
         * There is no countClientCancelled()
         * in your Service.
         *
         * countCancelled() is GLOBAL.
         *
         * Therefore cancelled count for one client
         * must be provided by a client-specific Service method.
         *
         * Until that method is added, use 0 here so that
         * Controller compilation does not fail.
         */
        long cancelledCodes = 0L;


        // -----------------------------------------------------
        // VALUES
        // -----------------------------------------------------

        BigDecimal issuedValue =
                service.clientIssuedValue(
                        clientId
                );


        BigDecimal redeemedValue =
                service.clientRedeemedValue(
                        clientId
                );


        BigDecimal activeValue =
                service.clientActiveValue(
                        clientId
                );


        BigDecimal expiredValue =
                service.clientExpiredValue(
                        clientId
                );


        // -----------------------------------------------------
        // REDEMPTION RATE
        // -----------------------------------------------------

        double redemptionRate =
                issuedCodes > 0
                        ? (
                        (double) redeemedCodes
                                / (double) issuedCodes
                ) * 100.0
                        : 0.0;


        // -----------------------------------------------------
        // RESPONSE
        // -----------------------------------------------------

        return ResponseEntity.ok(
                new ClientTruCardSummary(

                        issuedCodes,

                        issuedValue,

                        inactiveCodes,

                        activeCodes,

                        redeemedCodes,

                        redeemedValue,

                        expiredCodes,

                        expiredValue,

                        cancelledCodes,

                        redemptionRate
                )
        );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<TruBlankCodeResponse> getById(

            @PathVariable
            Long id

    ) {

        return ResponseEntity.ok(
                service.getById(
                        id
                )
        );
    }


    // =========================================================
    // GET BY SERIAL
    // =========================================================

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<TruBlankCodeResponse> getBySerial(

            @PathVariable
            String serialNumber

    ) {

        return ResponseEntity.ok(
                service.getBySerial(
                        serialNumber
                )
        );
    }


    // =========================================================
    // GET BY REFERENCE
    // =========================================================

    @GetMapping("/reference/{referenceNumber}")
    public ResponseEntity<TruBlankCodeResponse> getByReference(

            @PathVariable
            String referenceNumber

    ) {

        return ResponseEntity.ok(
                service.getByReference(
                        referenceNumber
                )
        );
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @GetMapping("/code/{codeNumber}")
    public ResponseEntity<TruBlankCodeResponse> getByCodeNumber(

            @PathVariable
            String codeNumber

    ) {

        return ResponseEntity.ok(
                service.getByCodeNumber(
                        codeNumber
                )
        );
    }


    // =========================================================
    // UNIVERSAL SEARCH
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<TruBlankCodeResponse> search(

            @RequestParam("value")
            String value

    ) {

        return ResponseEntity.ok(
                service.search(
                        value
                )
        );
    }


    // =========================================================
    // ASSIGN CLIENT
    // =========================================================

    @PutMapping("/{id}/client")
    public ResponseEntity<TruBlankCodeResponse> assignClient(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            AssignClientRequest request

    ) {

        return ResponseEntity.ok(
                service.assignClient(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<TruBlankCodeResponse> updateCode(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            UpdateBlankCodeRequest request

    ) {

        return ResponseEntity.ok(
                service.updateCode(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @PutMapping("/{id}/activate")
    public ResponseEntity<TruBlankCodeResponse> activateCode(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            ActivateBlankCodeRequest request

    ) {

        return ResponseEntity.ok(
                service.activateCode(
                        id,
                        request
                )
        );
    }


    // =========================================================
    // REDEEM
    // =========================================================

    @PutMapping("/{id}/redeem")
    public ResponseEntity<TruBlankCodeResponse> redeemCode(

            @PathVariable
            Long id

    ) {

        return ResponseEntity.ok(
                service.redeemCode(
                        id
                )
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<TruBlankCodeResponse> deactivateCode(

            @PathVariable
            Long id

    ) {

        return ResponseEntity.ok(
                service.deactivateCode(
                        id
                )
        );
    }


    // =========================================================
    // CANCEL
    // =========================================================

    @PutMapping("/{id}/cancel")
    public ResponseEntity<TruBlankCodeResponse> cancelCode(

            @PathVariable
            Long id

    ) {

        return ResponseEntity.ok(
                service.cancelCode(
                        id
                )
        );
    }


    // =========================================================
    // GET BY STATUS
    // =========================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TruBlankCodeResponse>> getByStatus(

            @PathVariable
            TruBlankCodeStatus status,

            Pageable pageable

    ) {

        return ResponseEntity.ok(
                service.getByStatus(
                        status,
                        pageable
                )
        );
    }


    // =========================================================
    // GLOBAL COUNT - INACTIVE
    // =========================================================

    @GetMapping("/count/inactive")
    public ResponseEntity<Long> countInactive() {

        return ResponseEntity.ok(
                service.countInactive()
        );
    }


    // =========================================================
    // GLOBAL COUNT - ACTIVE
    // =========================================================

    @GetMapping("/count/active")
    public ResponseEntity<Long> countActive() {

        return ResponseEntity.ok(
                service.countActive()
        );
    }


    // =========================================================
    // GLOBAL COUNT - REDEEMED
    // =========================================================

    @GetMapping("/count/redeemed")
    public ResponseEntity<Long> countRedeemed() {

        return ResponseEntity.ok(
                service.countRedeemed()
        );
    }


    // =========================================================
    // GLOBAL COUNT - EXPIRED
    // =========================================================

    @GetMapping("/count/expired")
    public ResponseEntity<Long> countExpired() {

        return ResponseEntity.ok(
                service.countExpired()
        );
    }


    // =========================================================
    // GLOBAL COUNT - CANCELLED
    // =========================================================

    @GetMapping("/count/cancelled")
    public ResponseEntity<Long> countCancelled() {

        return ResponseEntity.ok(
                service.countCancelled()
        );
    }


    // =========================================================
    // CLIENT COUNT - INACTIVE
    // =========================================================

    @GetMapping("/client/{clientId}/count/inactive")
    public ResponseEntity<Long> countClientInactive(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.countClientInactive(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT COUNT - ACTIVE
    // =========================================================

    @GetMapping("/client/{clientId}/count/active")
    public ResponseEntity<Long> countClientActive(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.countClientActive(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT COUNT - REDEEMED
    // =========================================================

    @GetMapping("/client/{clientId}/count/redeemed")
    public ResponseEntity<Long> countClientRedeemed(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.countClientRedeemed(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT COUNT - EXPIRED
    // =========================================================

    @GetMapping("/client/{clientId}/count/expired")
    public ResponseEntity<Long> countClientExpired(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.countClientExpired(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT VALUE - ISSUED
    // =========================================================

    @GetMapping("/client/{clientId}/value/issued")
    public ResponseEntity<BigDecimal> clientIssuedValue(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.clientIssuedValue(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT VALUE - REDEEMED
    // =========================================================

    @GetMapping("/client/{clientId}/value/redeemed")
    public ResponseEntity<BigDecimal> clientRedeemedValue(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.clientRedeemedValue(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT VALUE - ACTIVE
    // =========================================================

    @GetMapping("/client/{clientId}/value/active")
    public ResponseEntity<BigDecimal> clientActiveValue(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.clientActiveValue(
                        clientId
                )
        );
    }


    // =========================================================
    // CLIENT VALUE - EXPIRED
    // =========================================================

    @GetMapping("/client/{clientId}/value/expired")
    public ResponseEntity<BigDecimal> clientExpiredValue(

            @PathVariable
            Long clientId

    ) {

        return ResponseEntity.ok(
                service.clientExpiredValue(
                        clientId
                )
        );
    }


    // =========================================================
    // SUMMARY DTO
    // =========================================================

    public record ClientTruCardSummary(

            long issuedCodes,

            BigDecimal issuedValue,

            long inactiveCodes,

            long activeCodes,

            long redeemedCodes,

            BigDecimal redeemedValue,

            long expiredCodes,

            BigDecimal expiredValue,

            long cancelledCodes,

            double redemptionRate

    ) {
    }
}