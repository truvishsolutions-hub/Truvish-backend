package com.truvish.truvishbackend.corporateLogin.controller;

import com.truvish.truvishbackend.corporateLogin.dto.LoginRequest;
import com.truvish.truvishbackend.corporateLogin.dto.LoginResponse;
import com.truvish.truvishbackend.corporateLogin.service.ClientLoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/corporate/login")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientLoginController {

    private final ClientLoginService clientLoginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = clientLoginService.login(request);

        return ResponseEntity.ok(response);
    }

}
