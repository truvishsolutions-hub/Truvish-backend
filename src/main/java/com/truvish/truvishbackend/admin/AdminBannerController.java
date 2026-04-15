package com.truvish.truvishbackend.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/banner")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:5174",
                "http://localhost:5175",
                "http://localhost:5176"
        },
        allowCredentials = "true"
)
public class AdminBannerController {

    private final AdminConfigService service;

    public AdminBannerController(AdminConfigService service) {
        this.service = service;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBanner(
            @RequestParam(required = false) MultipartFile banner1,
            @RequestParam(required = false) MultipartFile banner2,
            @RequestParam(required = false) MultipartFile banner3,
            @RequestParam(required = false) MultipartFile banner4
    ) {
        try {
            AdminConfig cfg = service.getConfig();

            service.updateImage(cfg::setBanner1, banner1);
            service.updateImage(cfg::setBanner2, banner2);
            service.updateImage(cfg::setBanner3, banner3);
            service.updateImage(cfg::setBanner4, banner4);

            return ResponseEntity.ok(service.save(cfg));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Banner upload failed: " + e.getMessage());
        }
    }
}