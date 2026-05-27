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

    @Value("${app.mail.receiver}")
    private String receiverEmail;

    @Value("${app.mail.sender-email}")
    private String senderEmail;

    @Value("${app.mail.sender-name}")
    private String senderName;

    // =========================================================
    // SEND DEMO REQUEST EMAIL
    // =========================================================
    public void sendDemoRequestEmail(
            DemoRequestDto request
    ) {

        try {

            Resend resend =
                    new Resend(resendApiKey);

            String html = """
<!DOCTYPE html>
<html>
<body style="
    margin:0;
    padding:30px;
    background:#f4f6f9;
    font-family:Arial,sans-serif;
">

<div style="
    max-width:620px;
    margin:auto;
    background:#ffffff;
    border-radius:24px;
    overflow:hidden;
    box-shadow:0 8px 25px rgba(0,0,0,0.08);
">

    <div style="
        background:linear-gradient(135deg,#0E4A63,#13698f);
        padding:30px;
        text-align:center;
    ">

        <h1 style="
            color:white;
            margin:0;
            font-size:28px;
        ">
            New Demo Request
        </h1>

    </div>

    <div style="padding:35px;">

        <p style="
            font-size:16px;
            color:#555;
            line-height:1.7;
        ">
            A new demo request has been submitted from TruVish website.
        </p>

        <div style="
            background:#f8fafc;
            border-radius:18px;
            padding:25px;
            margin-top:25px;
        ">

            <p><b>Name:</b> %s</p>

            <p><b>Email:</b> %s</p>

            <p><b>Phone:</b> %s</p>

        </div>

        <div style="
            margin-top:30px;
            font-size:13px;
            color:#888;
            text-align:center;
        ">
            TruVish Solutions<br/>
            Rewards • Loyalty • Gift Vouchers
        </div>

    </div>

</div>

</body>
</html>
"""
                    .formatted(
                            safe(request.getName()),
                            safe(request.getEmail()),
                            safe(request.getPhone())
                    );

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from(senderName + " <" + senderEmail + ">")
                            .to(receiverEmail)
                            .replyTo(request.getEmail())
                            .subject("New Book a Demo Request - TruVish")
                            .html(html)
                            .build();

            resend.emails().send(params);

            System.out.println(
                    "DEMO REQUEST EMAIL SENT SUCCESSFULLY"
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "FAILED TO SEND EMAIL : "
                            + e.getMessage()
            );
        }
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