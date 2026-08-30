package com.truvish.truvishbackend.TruCard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/trucard")
public class TruCardController {

    // =========================================================
    // TRUCARD API STATUS
    // =========================================================

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTruCardStatus() {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("success", true);
        response.put("module", "TruCard");
        response.put("message", "TruCard API is running");

        return ResponseEntity.ok(response);
    }
}