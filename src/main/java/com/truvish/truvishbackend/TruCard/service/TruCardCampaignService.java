package com.truvish.truvishbackend.TruCard.service;

import com.truvish.truvishbackend.TruCard.dto.TruCardCampaignRequest;
import com.truvish.truvishbackend.TruCard.dto.TruCardCampaignResponse;
import com.truvish.truvishbackend.TruCard.entity.TruCardCampaign;
import com.truvish.truvishbackend.TruCard.repository.TruCardCampaignRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TruCardCampaignService {

    private final TruCardCampaignRepository campaignRepository;

    public TruCardCampaignService(
            TruCardCampaignRepository campaignRepository
    ) {
        this.campaignRepository = campaignRepository;
    }

    public List<TruCardCampaignResponse> getAllCampaigns() {

        return campaignRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TruCardCampaignResponse> getClientCampaigns(Long clientId) {

        return campaignRepository
                .findByClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<TruCardCampaignResponse> getActiveClientCampaigns(
            Long clientId
    ) {

        return campaignRepository
                .findByClientIdAndActiveTrueOrderByCreatedAtDesc(clientId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TruCardCampaignResponse getCampaignById(Long campaignId) {

        TruCardCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + campaignId
                                )
                        );

        return mapToResponse(campaign);
    }

    @Transactional
    public TruCardCampaignResponse createCampaign(
            TruCardCampaignRequest request
    ) {

        if (request.getClientId() == null) {
            throw new RuntimeException("Client ID is required");
        }

        if (request.getCampaignName() == null ||
                request.getCampaignName().isBlank()) {

            throw new RuntimeException("Campaign name is required");
        }

        if (request.getThemeId() == null) {
            throw new RuntimeException("Theme ID is required");
        }

        TruCardCampaign campaign = new TruCardCampaign();

        campaign.setClientId(request.getClientId());
        campaign.setCampaignName(request.getCampaignName());
        campaign.setThemeId(request.getThemeId());
        campaign.setThemeImage(request.getThemeImage());

        campaign.setActive(
                request.getActive() == null
                        ? true
                        : request.getActive()
        );

        return mapToResponse(
                campaignRepository.save(campaign)
        );
    }

    @Transactional
    public TruCardCampaignResponse updateCampaign(
            Long campaignId,
            TruCardCampaignRequest request
    ) {

        TruCardCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + campaignId
                                )
                        );

        if (request.getCampaignName() != null) {
            campaign.setCampaignName(request.getCampaignName());
        }

        if (request.getThemeId() != null) {
            campaign.setThemeId(request.getThemeId());
        }

        if (request.getThemeImage() != null) {
            campaign.setThemeImage(request.getThemeImage());
        }

        if (request.getActive() != null) {
            campaign.setActive(request.getActive());
        }

        return mapToResponse(
                campaignRepository.save(campaign)
        );
    }

    @Transactional
    public TruCardCampaignResponse activateCampaign(Long campaignId) {

        TruCardCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + campaignId
                                )
                        );

        campaign.setActive(true);

        return mapToResponse(
                campaignRepository.save(campaign)
        );
    }

    @Transactional
    public TruCardCampaignResponse deactivateCampaign(Long campaignId) {

        TruCardCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + campaignId
                                )
                        );

        campaign.setActive(false);

        return mapToResponse(
                campaignRepository.save(campaign)
        );
    }

    @Transactional
    public void deleteCampaign(Long campaignId) {

        TruCardCampaign campaign =
                campaignRepository.findById(campaignId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "TruCard Campaign not found with id: "
                                                + campaignId
                                )
                        );

        campaignRepository.delete(campaign);
    }

    private TruCardCampaignResponse mapToResponse(
            TruCardCampaign campaign
    ) {

        TruCardCampaignResponse response =
                new TruCardCampaignResponse();

        response.setId(campaign.getId());
        response.setClientId(campaign.getClientId());
        response.setCampaignName(campaign.getCampaignName());
        response.setThemeId(campaign.getThemeId());
        response.setThemeImage(campaign.getThemeImage());
        response.setActive(campaign.getActive());
        response.setCreatedAt(campaign.getCreatedAt());
        response.setUpdatedAt(campaign.getUpdatedAt());

        return response;
    }
}