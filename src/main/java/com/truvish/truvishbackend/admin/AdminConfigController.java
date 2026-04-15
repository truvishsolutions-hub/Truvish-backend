package com.truvish.truvishbackend.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/config")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        },
        allowCredentials = "true"
)
public class AdminConfigController {

    private final AdminConfigService service;

    public AdminConfigController(AdminConfigService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<AdminConfig> getConfig() {
        return ResponseEntity.ok(service.getConfig());
    }

    @PostMapping
    public ResponseEntity<AdminConfig> saveConfig(@RequestBody AdminConfig config) {
        return ResponseEntity.ok(service.save(config));
    }
}