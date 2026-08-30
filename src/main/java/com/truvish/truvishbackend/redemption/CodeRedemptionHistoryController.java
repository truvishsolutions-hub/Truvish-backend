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
public class CodeRedemptionHistoryController {

    private final CodeRedemptionHistoryService service;


    public CodeRedemptionHistoryController(CodeRedemptionHistoryService service) {
        this.service = service;
    }

    @GetMapping("/code/{code}")
    public List<CodeRedemptionHistoryResponse> byCode(@PathVariable String code) {
        return service.byCode(code == null ? null : code.trim());
    }

    @GetMapping("/phone/{phone}")
    public List<CodeRedemptionHistoryResponse> byPhone(@PathVariable String phone) {
        return service.byPhone(phone == null ? null : phone.trim());
    }
}