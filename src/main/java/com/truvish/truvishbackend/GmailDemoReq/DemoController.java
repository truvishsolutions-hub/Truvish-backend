 package com.truvish.truvishbackend.GmailDemoReq;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DemoController {

    private final EmailService emailService;

    public DemoController(
            EmailService emailService
    ) {
        this.emailService = emailService;
    }

    @PostMapping("/book-demo")
    public ResponseEntity<?> bookDemo(
            @Valid @RequestBody DemoRequest request
    ) {

        try {

            DemoRequestDto dto =
                    new DemoRequestDto();

            dto.setName(
                    request.getName()
            );

            dto.setEmail(
                    request.getEmail()
            );

            dto.setPhone(
                    request.getPhone()
            );

            emailService.sendDemoRequestEmail(dto);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Demo request sent successfully"
                    )
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message",
                                    e.getMessage() == null
                                            ? "Email could not be sent"
                                            : e.getMessage()
                            )
                    );
        }
    }
}
