package com.truvish.truvishbackend.TruCard.controller;

import com.truvish.truvishbackend.TruCard.dto.TruCardCampaignRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardCampaignResponse;
import com.truvish.truvishbackend.TruCard.service.TruCardCampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trucard/campaigns")
public class TruCardCampaignController {

    private final TruCardCampaignService truCardCampaignService;

    public TruCardCampaignController(
            TruCardCampaignService truCardCampaignService
    ) {
        this.truCardCampaignService = truCardCampaignService;
    }


    // =========================================================
    // CREATE CAMPAIGN
    // =========================================================

    @PostMapping
    public ResponseEntity<TruCardCampaignResponse> createCampaign(
            @RequestBody TruCardCampaignRequest request
    ) {

        TruCardCampaignResponse response =
                truCardCampaignService.createCampaign(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET ALL CAMPAIGNS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<TruCardCampaignResponse>> getAllCampaigns() {

        List<TruCardCampaignResponse> campaigns =
                truCardCampaignService.getAllCampaigns();

        return ResponseEntity.ok(campaigns);
    }


    // =========================================================
    // GET CAMPAIGN BY ID
    // =========================================================

    @GetMapping("/{campaignId}")
    public ResponseEntity<TruCardCampaignResponse> getCampaignById(
            @PathVariable Long campaignId
    ) {

        TruCardCampaignResponse response =
                truCardCampaignService.getCampaignById(
                        campaignId
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // UPDATE CAMPAIGN
    // =========================================================

    @PutMapping("/{campaignId}")
    public ResponseEntity<TruCardCampaignResponse> updateCampaign(
            @PathVariable Long campaignId,
            @RequestBody TruCardCampaignRequest request
    ) {

        TruCardCampaignResponse response =
                truCardCampaignService.updateCampaign(
                        campaignId,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE CAMPAIGN
    // =========================================================

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<Void> deleteCampaign(
            @PathVariable Long campaignId
    ) {

        truCardCampaignService.deleteCampaign(
                campaignId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}