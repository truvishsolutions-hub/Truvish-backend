package com.truvish.truvishbackend.TruvishCode;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("/api/truvish")
@CrossOrigin(origins = "*")
public class TruvishCodeController {

    private final TruvishCodeService service;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public TruvishCodeController(
            TruvishCodeService service
    ) {
        this.service = service;
    }


    // =========================================================
    // CREATE TRUVISH CODE
    // =========================================================

    @PostMapping("/code")
    public ResponseEntity<?> create(
            @RequestBody TruvishCode code
    ) {

        try {

            return ResponseEntity.ok(
                    service.create(code)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Failed to create Truvish code",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // VERIFY TRUVISH CODE
    // =========================================================

    @GetMapping("/verify/{code}")
    public ResponseEntity<?> verify(
            @PathVariable String code
    ) {

        try {

            if (code == null || code.trim().isEmpty()) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Code is required"
                                )
                        );
            }

            return ResponseEntity.ok(
                    service.verifyCode(
                            code.trim()
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    e.getMessage() == null
                                            ? "Invalid code"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // CREATE / ASSIGN DIGITAL VOUCHER
    // =========================================================
    //
    // FRONTEND:
    //
    // POST
    // /api/truvish/create-voucher
    //
    // FLOW:
    //
    // 1. Validate client
    // 2. Check client balance
    // 3. Create voucher codes
    // 4. Debit client wallet
    // 5. Create wallet DEBIT history
    // 6. Return generated codes
    //
    // =========================================================

    @PostMapping("/create-voucher")
    public ResponseEntity<?> createVoucher(
            @Valid
            @RequestBody CodeAssignmentDto dto
    ) {

        try {

            return ResponseEntity.ok(
                    service.updateClient(dto)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Failed to create voucher",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // UPDATE CLIENT / CREATE DIGITAL VOUCHER
    // =========================================================
    //
    // POST:
    //
    // /api/truvish/update-client
    //
    // IMPORTANT:
    //
    // Ye endpoint /create-voucher ke same service ko call karta hai.
    //
    // Isliye dono endpoints ka behaviour same rahega.
    //
    // Postman testing ke liye:
    //
    // POST
    // http://localhost:8080/api/truvish/update-client
    //
    // =========================================================

    @PostMapping("/update-client")
    public ResponseEntity<?> updateClient(
            @Valid
            @RequestBody CodeAssignmentDto dto
    ) {

        try {

            return ResponseEntity.ok(
                    service.updateClient(dto)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Failed to update client",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // CLIENT HISTORY BY CLIENT NAME
    // =========================================================
    //
    // GET:
    //
    // /api/truvish/history/Admin2
    //
    // =========================================================

    @GetMapping("/history/{clientName}")
    public ResponseEntity<?> history(
            @PathVariable String clientName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        try {

            if (
                    clientName == null
                            || clientName.trim().isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Client name is required"
                                )
                        );
            }


            if (page < 0) {
                page = 0;
            }


            if (size <= 0) {
                size = 20;
            }


            return ResponseEntity.ok(
                    service.history(
                            clientName.trim(),
                            page,
                            size
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "message",
                                    "Failed to load client history",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // CLIENT HISTORY BY CLIENT ID
    // =========================================================
    //
    // FRONTEND:
    //
    // GET
    // /api/truvish/history/client/20
    //
    // =========================================================

    @GetMapping("/history/client/{clientId}")
    public ResponseEntity<?> historyByClientId(
            @PathVariable Long clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        try {

            if (
                    clientId == null
                            || clientId <= 0
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Invalid clientId"
                                )
                        );
            }


            if (page < 0) {
                page = 0;
            }


            if (size <= 0) {
                size = 20;
            }


            return ResponseEntity.ok(
                    service.historyByClientId(
                            clientId,
                            page,
                            size
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "message",
                                    "Failed to load client history",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }


    // =========================================================
    // UPLOAD CLIENT IMAGE
    // =========================================================

    @PostMapping(
            value = "/upload-client-img",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadClientImg(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            // -------------------------------------------------
            // CHECK FILE
            // -------------------------------------------------

            if (
                    file == null
                            || file.isEmpty()
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "File is empty"
                                )
                        );
            }


            // -------------------------------------------------
            // CHECK CONTENT TYPE
            // -------------------------------------------------

            String contentType =
                    file.getContentType();


            if (
                    contentType == null
                            || !contentType.startsWith("image/")
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Only image files are allowed"
                                )
                        );
            }


            // -------------------------------------------------
            // ORIGINAL FILE NAME
            // -------------------------------------------------

            String original =
                    StringUtils.cleanPath(
                            Objects.requireNonNull(
                                    file.getOriginalFilename()
                            )
                    );


            // -------------------------------------------------
            // FILE EXTENSION
            // -------------------------------------------------

            String ext = "";

            int dot =
                    original.lastIndexOf('.');


            if (dot >= 0) {

                ext =
                        original
                                .substring(dot)
                                .toLowerCase();
            }


            // -------------------------------------------------
            // ALLOWED EXTENSIONS
            // -------------------------------------------------

            List<String> allowed =
                    Arrays.asList(
                            ".png",
                            ".jpg",
                            ".jpeg",
                            ".webp"
                    );


            if (
                    !ext.isEmpty()
                            && !allowed.contains(ext)
            ) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                Map.of(
                                        "message",
                                        "Allowed: png, jpg, jpeg, webp"
                                )
                        );
            }


            // -------------------------------------------------
            // GENERATE UNIQUE FILE NAME
            // -------------------------------------------------

            String fileName =
                    UUID.randomUUID()
                            + (
                            ext.isEmpty()
                                    ? ".png"
                                    : ext
                    );


            // -------------------------------------------------
            // UPLOAD DIRECTORY
            // -------------------------------------------------

            Path uploadDir =
                    Paths.get(
                            "uploads",
                            "client"
                    );


            Files.createDirectories(
                    uploadDir
            );


            // -------------------------------------------------
            // TARGET FILE
            // -------------------------------------------------

            Path target =
                    uploadDir.resolve(
                            fileName
                    );


            // -------------------------------------------------
            // SAVE FILE
            // -------------------------------------------------

            Files.copy(
                    file.getInputStream(),
                    target,
                    StandardCopyOption.REPLACE_EXISTING
            );


            // -------------------------------------------------
            // PUBLIC PATH
            // -------------------------------------------------

            String publicPath =
                    "/uploads/client/"
                            + fileName;


            // -------------------------------------------------
            // RESPONSE
            // -------------------------------------------------

            Map<String, Object> response =
                    new HashMap<>();


            response.put(
                    "clientImg",
                    publicPath
            );


            return ResponseEntity.ok(
                    response
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Upload failed",
                                    "error",
                                    e.getMessage() == null
                                            ? "Unknown error"
                                            : e.getMessage()
                            )
                    );
        }
    }
}