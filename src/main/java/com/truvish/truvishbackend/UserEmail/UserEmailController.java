package com.truvish.truvishbackend.UserEmail;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user-email")
public class UserEmailController {

    private final UserEmailService service;

    public UserEmailController(
            UserEmailService service
    ) {
        this.service = service;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendVoucherEmail(
            @Valid @RequestBody UserEmailRequest request
    ) {

        try {

            service.sendVoucherEmail(request);

            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message",
                            "Voucher email sent successfully"
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
                                    e.getMessage()
                            )
                    );
        }
    }
}