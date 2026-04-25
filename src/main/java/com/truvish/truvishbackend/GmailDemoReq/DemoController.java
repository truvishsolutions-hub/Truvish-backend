package com.truvish.truvishbackend.GmailDemoReq;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoController {

    private final EmailService emailService;

    public DemoController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/book-demo")
    public ResponseEntity<?> bookDemo(@Valid @RequestBody DemoRequest request) {
        try {
            emailService.sendDemoRequestEmail(request);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Demo request sent successfully"
            ));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", e.getMessage() == null ? "Email could not be sent" : e.getMessage()
            ));
        }
    }
}