package com.truvish.truvishbackend.GmailDemoReq;

import com.truvish.truvishbackend.DemoRequest.dto.DemoRequestDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.receiver}")
    private String receiverEmail;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =========================================================
    // SEND DEMO REQUEST EMAIL
    // =========================================================
    public void sendDemoRequestEmail(
            DemoRequestDto request
    ) throws MessagingException {

        MimeMessage message =
                mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(
                        message,
                        false,
                        "UTF-8"
                );

        helper.setFrom(senderEmail);

        helper.setTo(receiverEmail);

        helper.setReplyTo(request.getEmail());

        helper.setSubject(
                "New Book a Demo Request - TruVish"
        );

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

        helper.setText(emailBody, false);

        mailSender.send(message);
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