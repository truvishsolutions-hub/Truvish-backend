package com.truvish.truvishbackend.TruvishCode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.*;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/truvish")
public class TruvishCodeController {

    private final TruvishCodeService service;

    public TruvishCodeController(TruvishCodeService service) {
        this.service = service;
    }

    @PostMapping("/code")
    public ResponseEntity<?> create(@RequestBody TruvishCode code) {
        try {
            return ResponseEntity.ok(service.create(code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<?> verify(@PathVariable String code) {
        try {
            return ResponseEntity.ok(service.verifyCode(code));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Duplicate Problem//

    @PostMapping("/create-voucher")
    public ResponseEntity<?> updateClient(
            @Valid
            @RequestBody CodeAssignmentDto dto
    ) {
        return ResponseEntity.ok(
                service.updateClient(dto)
        );
    }

    // ✅ now returns List<ClientHistoryItem>
    @GetMapping("/history/{clientName}")
    public ResponseEntity<?> history(
            @PathVariable String clientName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            return ResponseEntity.ok(
                    service.history(clientName, page, size)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(
            value = "/upload-client-img",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadClientImg(
            @RequestParam("file") MultipartFile file
    ) {
        try {

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            String original = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename())
            );

            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot).toLowerCase();

            List<String> allowed = Arrays.asList(".png", ".jpg", ".jpeg", ".webp");
            if (!ext.isEmpty() && !allowed.contains(ext)) {
                return ResponseEntity.badRequest()
                        .body("Allowed: png, jpg, jpeg, webp");
            }

            String fileName = UUID.randomUUID() + (ext.isEmpty() ? ".png" : ext);

            Path uploadDir = Paths.get("uploads", "client");
            Files.createDirectories(uploadDir);

            Path target = uploadDir.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String publicPath = "/uploads/client/" + fileName;

            Map<String, Object> response = new HashMap<>();
            response.put("clientImg", publicPath);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Upload failed: " + e.getMessage());
        }
    }
}