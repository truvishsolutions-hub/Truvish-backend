package com.truvish.truvishbackend.redemption;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/redemption-history")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        },
        allowCredentials = "true"
)
public class RedemptionHistoryController {

    private final UserRedemptionService service;

    public RedemptionHistoryController(UserRedemptionService service) {
        this.service = service;
    }

    @GetMapping
    public List<RedemptionHistoryItemResponse> getHistory(
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String code
    ) {
        return service.getHistory(phone, code);
    }

    @PostMapping("/search")
    public List<RedemptionHistoryItemResponse> getHistoryByBody(
            @RequestBody RedemptionHistoryRequest request
    ) {
        return service.getHistory(request.getPhone(), request.getCode());
    }
}