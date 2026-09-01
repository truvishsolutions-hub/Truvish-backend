package com.truvish.truvishbackend.redemption;

import com.truvish.truvishbackend.TruOpeAdmin.ClientHistoryItem;
import com.truvish.truvishbackend.TruOpeAdmin.TruvishCodeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/redemption-history")
public class RedemptionHistoryController {

    private final TruvishCodeService truvishCodeService;

    public RedemptionHistoryController(TruvishCodeService truvishCodeService) {
        this.truvishCodeService = truvishCodeService;
    }

    @GetMapping("/client/{clientId}")
    public List<ClientHistoryItem> getClientHistory(@PathVariable Long clientId) {
        // Return all history for the client (max 1000 records)
        return truvishCodeService.historyByClientId(clientId, 0, 1000);
    }
}