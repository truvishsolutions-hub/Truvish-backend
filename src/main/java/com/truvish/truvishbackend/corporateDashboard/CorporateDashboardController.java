package com.truvish.truvishbackend.corporateDashboard;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/corporate/dashboard")
@RequiredArgsConstructor
public class CorporateDashboardController {

    private final CorporateDashboardService corporateDashboardService;


    // =========================================================
    // GET CORPORATE DASHBOARD
    // =========================================================

    @GetMapping("/{clientId}")
    public ResponseEntity<CorporateDashboardResponse> getDashboard(
            @PathVariable Long clientId
    ) {

        CorporateDashboardResponse response =
                corporateDashboardService.getDashboard(clientId);

        return ResponseEntity.ok(response);
    }
}