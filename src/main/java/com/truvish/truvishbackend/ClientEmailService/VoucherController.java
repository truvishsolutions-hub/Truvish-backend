package com.truvish.truvishbackend.ClientEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://localhost:3000"
        },
        allowedHeaders = "*",
        methods = {
                RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS
        }
)
public class VoucherController {

    @Autowired
    private ClientEmailService emailService;

    @PostMapping("/send")
    public String sendVoucher(
            @RequestBody VoucherRequest request
    ) {

        // =====================================================
        // CLIENT LOGO FULL PATH
        // =====================================================
        String clientLogoPath = null;

        if (
                request.getClientLogo() != null &&
                        !request.getClientLogo().trim().isEmpty()
        ) {

            clientLogoPath =
                    "uploads/" +
                            request.getClientLogo();
        }

        // =====================================================
        // SEND EMAIL
        // =====================================================
        emailService.sendVoucher(
                request.getEmail(),
                request.getName(),
                request.getVoucherCode(),
                clientLogoPath
        );

        return "Voucher Sent Successfully";
    }
}