package com.truvish.truvishbackend.ClientEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    @Autowired
    private ClientEmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendVoucher(
            @RequestBody VoucherRequest request
    ) {

        try {

            // =====================================================
            // CLIENT LOGO
            // =====================================================

            String clientLogoPath = null;

            if (
                    request.getClientLogo() != null
                            &&
                            !request.getClientLogo().trim().isEmpty()
            ) {

                clientLogoPath =
                        request.getClientLogo();
            }

            // =====================================================
            // DEFAULT VALUES
            // =====================================================

            Integer validityDays =
                    request.getValidityDays() == null

                            ? 60

                            : request.getValidityDays();

            String companyName =
                    request.getCompanyName() == null
                            ||
                            request.getCompanyName().isBlank()

                            ? "TruVish"

                            : request.getCompanyName();

            // =====================================================
            // SEND EMAIL
            // =====================================================

            emailService.sendVoucher(

                    request.getEmail(),

                    request.getName(),

                    request.getVoucherCode(),

                    clientLogoPath,

                    validityDays,

                    companyName
            );

            return ResponseEntity.ok(

                    Map.of(

                            "success", true,

                            "message",
                            "Voucher Sent Successfully"
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

                                            ? "Failed to send voucher"

                                            : e.getMessage()
                            )
                    );
        }
    }
}