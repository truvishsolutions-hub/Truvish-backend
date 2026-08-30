package com.truvish.truvishbackend.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/theme")

public class AdminThemeController {

    private final AdminConfigService service;

    public AdminThemeController(AdminConfigService service) {
        this.service = service;
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadTheme(
            @RequestParam(required = false) String themeName1,
            @RequestParam(required = false) String themeName2,
            @RequestParam(required = false) String themeName3,
            @RequestParam(required = false) String themeName4,

            @RequestParam(required = false) MultipartFile themeImg1,
            @RequestParam(required = false) MultipartFile themeImg2,
            @RequestParam(required = false) MultipartFile themeImg3,
            @RequestParam(required = false) MultipartFile themeImg4,

            @RequestParam(required = false) MultipartFile img1,
            @RequestParam(required = false) String img1Name,

            @RequestParam(required = false) MultipartFile img2,
            @RequestParam(required = false) String img2Name,

            @RequestParam(required = false) MultipartFile img3,
            @RequestParam(required = false) String img3Name,

            @RequestParam(required = false) MultipartFile img4,
            @RequestParam(required = false) String img4Name,

            @RequestParam(required = false) MultipartFile img6,
            @RequestParam(required = false) String img6Name,

            @RequestParam(required = false) MultipartFile img7,
            @RequestParam(required = false) String img7Name,

            @RequestParam(required = false) MultipartFile img8,
            @RequestParam(required = false) String img8Name,

            @RequestParam(required = false) MultipartFile img9,
            @RequestParam(required = false) String img9Name,

            @RequestParam(required = false) MultipartFile img11,
            @RequestParam(required = false) String img11Name,

            @RequestParam(required = false) MultipartFile img12,
            @RequestParam(required = false) String img12Name,

            @RequestParam(required = false) MultipartFile img13,
            @RequestParam(required = false) String img13Name,

            @RequestParam(required = false) MultipartFile img14,
            @RequestParam(required = false) String img14Name,

            @RequestParam(required = false) MultipartFile img16,
            @RequestParam(required = false) String img16Name,

            @RequestParam(required = false) MultipartFile img17,
            @RequestParam(required = false) String img17Name,

            @RequestParam(required = false) MultipartFile img18,
            @RequestParam(required = false) String img18Name,

            @RequestParam(required = false) MultipartFile img19,
            @RequestParam(required = false) String img19Name
    ) {
        try {
            AdminConfig cfg = service.getConfig();

            if (themeName1 != null) cfg.setThemeName1(themeName1);
            if (themeName2 != null) cfg.setThemeName2(themeName2);
            if (themeName3 != null) cfg.setThemeName3(themeName3);
            if (themeName4 != null) cfg.setThemeName4(themeName4);

            service.updateImage(cfg::setThemeImg1, themeImg1);
            service.updateImage(cfg::setThemeImg2, themeImg2);
            service.updateImage(cfg::setThemeImg3, themeImg3);
            service.updateImage(cfg::setThemeImg4, themeImg4);

            service.updateImageWithName(cfg::setImg1, cfg::setImg1Name, img1, img1Name);
            service.updateImageWithName(cfg::setImg2, cfg::setImg2Name, img2, img2Name);
            service.updateImageWithName(cfg::setImg3, cfg::setImg3Name, img3, img3Name);
            service.updateImageWithName(cfg::setImg4, cfg::setImg4Name, img4, img4Name);

            service.updateImageWithName(cfg::setImg6, cfg::setImg6Name, img6, img6Name);
            service.updateImageWithName(cfg::setImg7, cfg::setImg7Name, img7, img7Name);
            service.updateImageWithName(cfg::setImg8, cfg::setImg8Name, img8, img8Name);
            service.updateImageWithName(cfg::setImg9, cfg::setImg9Name, img9, img9Name);

            service.updateImageWithName(cfg::setImg11, cfg::setImg11Name, img11, img11Name);
            service.updateImageWithName(cfg::setImg12, cfg::setImg12Name, img12, img12Name);
            service.updateImageWithName(cfg::setImg13, cfg::setImg13Name, img13, img13Name);
            service.updateImageWithName(cfg::setImg14, cfg::setImg14Name, img14, img14Name);

            service.updateImageWithName(cfg::setImg16, cfg::setImg16Name, img16, img16Name);
            service.updateImageWithName(cfg::setImg17, cfg::setImg17Name, img17, img17Name);
            service.updateImageWithName(cfg::setImg18, cfg::setImg18Name, img18, img18Name);
            service.updateImageWithName(cfg::setImg19, cfg::setImg19Name, img19, img19Name);

            return ResponseEntity.ok(service.save(cfg));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Theme upload failed: " + e.getMessage());
        }
    }
}