package com.truvish.truvishbackend.voucherinventory;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/voucher-inventory")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        },
        allowCredentials = "true"
)
public class VoucherInventoryController {

    private final VoucherInventoryService voucherInventoryService;

    public VoucherInventoryController(VoucherInventoryService voucherInventoryService) {
        this.voucherInventoryService = voucherInventoryService;
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        return ResponseEntity.ok(voucherInventoryService.getAvailableBrands());
    }

    @GetMapping("/denominations")
    public ResponseEntity<List<BigDecimal>> getDenominations(@RequestParam String brandName) {
        return ResponseEntity.ok(voucherInventoryService.getAvailableDenominations(brandName));
    }

    @PostMapping
    public ResponseEntity<List<VoucherInventory>> addInventory(@Valid @RequestBody AddVoucherInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(voucherInventoryService.addInventory(request));
    }

    @GetMapping("/summary")
    public ResponseEntity<List<VoucherInventorySummaryResponse>> getSummary() {
        return ResponseEntity.ok(voucherInventoryService.getSummary());
    }

    @GetMapping("/counter")
    public ResponseEntity<List<VoucherInventoryCounterResponse>> getCounter(
            @RequestParam String brandName,
            @RequestParam BigDecimal denomination
    ) {
        return ResponseEntity.ok(voucherInventoryService.getCounter(brandName, denomination));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherInventoryService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/redeem")
    public ResponseEntity<VoucherRedeemResponse> redeem(@Valid @RequestBody VoucherRedeemRequest request) {
        return ResponseEntity.ok(voucherInventoryService.redeem(request));
    }
}
