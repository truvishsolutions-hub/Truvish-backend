package com.truvish.truvishbackend.DemoRequest.controller;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;
import com.truvish.truvishbackend.DemoRequest.entity.DemoRequest;
import com.truvish.truvishbackend.DemoRequest.service.DemoRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demo-requests")
@CrossOrigin(origins = "http://localhost:3000")
public class DemoRequestController {

    private final DemoRequestService demoRequestService;

    public DemoRequestController(DemoRequestService demoRequestService) {
        this.demoRequestService = demoRequestService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createDemoRequest(
            @Valid @RequestBody DemoRequestDto demoRequestDto
    ) {
        DemoRequest saved = demoRequestService.saveDemoRequest(demoRequestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Demo request saved successfully");
        response.put("data", saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DemoRequest>> getAllDemoRequests() {
        return ResponseEntity.ok(demoRequestService.getAllDemoRequests());
    }
}