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

    // For database save flow: /api/demo-requests
    public void sendDemoRequestEmail(DemoRequestDto request) throws MessagingException {
        sendEmail(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                null,
                null
        );
    }

    // For old Gmail flow: /api/book-demo
    public void sendDemoRequestEmail(DemoRequest request) throws MessagingException {
        sendEmail(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getCompany(),
                request.getMessage()
        );
    }

    private void sendEmail(
            String name,
            String email,
            String phone,
            String company,
            String userMessage
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(receiverEmail);
        helper.setReplyTo(email);
        helper.setSubject("New Book a Demo Request - TruVish");

        String emailBody =
                "New Book a Demo Request - TruVish\n\n" +
                        "Name: " + safe(name) + "\n" +
                        "Email: " + safe(email) + "\n" +
                        "Phone: " + safe(phone) + "\n" +
                        "This email was sent from TruVish Book a Demo form.";

        helper.setText(emailBody, false);

        mailSender.send(message);
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not provided" : value.trim();
    }
}