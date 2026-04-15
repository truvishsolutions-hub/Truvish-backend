package com.truvish.truvishbackend.redemption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redeem")
public class RedemptionController {

    private final RedemptionService service;

    public RedemptionController(RedemptionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> redeem(
            @RequestParam String code,
            @RequestParam String phone,
            @RequestParam String brandName,
            @RequestParam Long selectedValue,
            @RequestParam(required = false) String brandLogo
    ) {
        try {
            RedemptionResponse result = service.redeemAndReturnVoucher(
                    code,
                    phone,
                    brandName,
                    selectedValue,
                    brandLogo
            );
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PostMapping("/body")
    public ResponseEntity<?> redeemByBody(@RequestBody RedeemRequest request) {
        try {
            RedemptionResponse result = service.redeemAndReturnVoucher(
                    request.getCode(),
                    request.getPhone(),
                    request.getBrandName(),
                    request.getSelectedValue(),
                    request.getBrandLogo()
            );
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }
}