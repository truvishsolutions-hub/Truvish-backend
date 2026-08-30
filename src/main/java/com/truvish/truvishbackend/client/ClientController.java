package com.truvish.truvishbackend.client;

import com.truvish.truvishbackend.common.FileStorageService;

import jakarta.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;
    private final ClientRepository repo;
    private final FileStorageService storage;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ClientController(
            ClientService service,
            ClientRepository repo,
            FileStorageService storage
    ) {

        this.service = service;
        this.repo = repo;
        this.storage = storage;
    }


    // =========================================================
    // CHECK MOBILE EXISTS
    // =========================================================

    @GetMapping("/exists")
    public Map<String, Object> exists(
            @RequestParam String mobile
    ) {

        boolean ok =
                service.existsByMobile(
                        mobile
                );


        return Map.of(
                "exists",
                ok
        );
    }


    // =========================================================
    // GET CLIENT BY MOBILE
    // =========================================================

    @GetMapping("/by-mobile")
    public Client byMobile(
            @RequestParam String mobile
    ) {

        return service.getByMobile(
                mobile
        );
    }


    // =========================================================
    // SEARCH CLIENTS
    // =========================================================
    //
    // GET:
    //
    // /api/clients/search?value=12
    //
    // Searches:
    //
    // Company Name
    // Client Name
    // Mobile Number
    // Email
    //
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<List<Client>> search(
            @RequestParam(
                    value = "value",
                    required = false
            )
            String value
    ) {

        return ResponseEntity.ok(
                service.search(value)
        );
    }


    // =========================================================
    // CREATE CLIENT
    // =========================================================

    @PostMapping(
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Client> create(

            @Valid
            @RequestPart("client")
            ClientRequest req,

            @RequestPart(
                    value = "logo",
                    required = false
            )
            MultipartFile logo

    ) throws Exception {

        Client saved =
                service.create(
                        req,
                        logo
                );


        return ResponseEntity
                .status(
                        HttpStatus.CREATED
                )
                .body(saved);
    }


    // =========================================================
    // GET ALL CLIENTS
    // =========================================================

    @GetMapping
    public List<Client> list() {

        return service.list();
    }


    // =========================================================
    // CLIENT OVERVIEW
    // =========================================================

    @GetMapping("/overview")
    public List<ClientOverviewResponse>
    overview() {

        return service.listOverview();
    }


    // =========================================================
    // DASHBOARD SUMMARY
    // =========================================================

    @GetMapping("/dashboard-summary")
    public DashboardSummaryResponse
    dashboardSummary() {

        return service.getDashboardSummary();
    }


    // =========================================================
    // GET CLIENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public Client get(
            @PathVariable Long id
    ) {

        return service.get(id);
    }


    // =========================================================
    // UPDATE CLIENT
    // =========================================================

    @PutMapping(
            value = "/{id}",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public Client update(

            @PathVariable Long id,

            @Valid
            @RequestPart("client")
            ClientRequest req,

            @RequestPart(
                    value = "logo",
                    required = false
            )
            MultipartFile logo

    ) throws Exception {

        return service.update(
                id,
                req,
                logo
        );
    }


    // =========================================================
    // DELETE CLIENT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {

        service.delete(id);


        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Deleted"
                )
        );
    }


    // =========================================================
    // GET CLIENT LOGO
    // =========================================================

    @GetMapping("/{id}/logo")
    public ResponseEntity<Resource> logo(
            @PathVariable Long id
    ) {

        Client c =
                repo.findById(id)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Client not found: "
                                                        + id
                                        )
                        );


        Resource res =
                storage.load(
                        c.getLogoImg()
                );


        if (res == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        try {

            String contentType =
                    Files.probeContentType(
                            res.getFile().toPath()
                    );


            if (contentType == null) {

                contentType =
                        MediaType
                                .APPLICATION_OCTET_STREAM_VALUE;
            }


            return ResponseEntity
                    .ok()
                    .contentType(
                            MediaType.parseMediaType(
                                    contentType
                            )
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\""
                                    + res.getFilename()
                                    + "\""
                    )
                    .body(res);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}