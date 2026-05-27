package com.truvish.truvishbackend.GmailDemoReq;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.mail.sender-name}")
    private String senderName;

    @Value("${app.mail.sender-email}")
    private String senderEmail;

    @Value("${app.mail.receiver}")
    private String receiverEmail;

    // =========================================================
    // SEND DEMO REQUEST EMAIL
    // =========================================================
    public void sendDemoRequestEmail(
            DemoRequestDto request
    ) {

        Resend resend = new Resend(resendApiKey);

        String emailBody = """
New Book a Demo Request - TruVish

Name: %s
Email: %s
Phone: %s

This email was sent from TruVish Book a Demo form.
"""
                .formatted(
                        safe(request.getName()),
                        safe(request.getEmail()),
                        safe(request.getPhone())
                );

        CreateEmailOptions params =
                CreateEmailOptions.builder()
                        .from(
                                senderName
                                        + " <"
                                        + senderEmail
                                        + ">"
                        )
                        .to(receiverEmail)
                        .replyTo(request.getEmail())
                        .subject("New Book a Demo Request - TruVish")
                        .text(emailBody)
                        .build();

        resend.emails().send(params);
    }

    // =========================================================
    // SAFE VALUE
    // =========================================================
    private String safe(String value) {

        return value == null ||
                value.isBlank()

                ? "Not provided"

                : value.trim();
    }
}