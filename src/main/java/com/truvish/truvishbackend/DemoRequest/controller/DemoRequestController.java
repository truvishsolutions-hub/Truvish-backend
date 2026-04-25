package com.truvish.truvishbackend.DemoRequest.controller;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;
import com.truvish.truvishbackend.DemoRequest.entity.DemoRequest;
import com.truvish.truvishbackend.DemoRequest.service.DemoRequestService;
import com.truvish.truvishbackend.GmailDemoReq.EmailService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demo-requests")
public class DemoRequestController {

    private static final Logger logger = LoggerFactory.getLogger(DemoRequestController.class);

    private final DemoRequestService demoRequestService;
    private final EmailService emailService;

    public DemoRequestController(
            DemoRequestService demoRequestService,
            EmailService emailService
    ) {
        this.demoRequestService = demoRequestService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDemoRequest(
            @Valid @RequestBody DemoRequestDto demoRequestDto
    ) {
        DemoRequest saved = demoRequestService.saveDemoRequest(demoRequestDto);

        boolean emailSent = true;
        String emailMessage = "Email sent successfully";

        try {
            emailService.sendDemoRequestEmail(demoRequestDto);
        } catch (Exception e) {
            emailSent = false;
            emailMessage = "Demo request saved, but email could not be sent.";
            logger.error("Failed to send demo request email", e);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Demo request saved successfully");
        response.put("emailSent", emailSent);
        response.put("emailMessage", emailMessage);
        response.put("data", saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DemoRequest>> getAllDemoRequests() {
        return ResponseEntity.ok(demoRequestService.getAllDemoRequests());
    }
}
