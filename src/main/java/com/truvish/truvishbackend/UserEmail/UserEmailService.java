package com.truvish.truvishbackend.UserEmail;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserEmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.mail.sender-email}")
    private String senderEmail;

    @Value("${app.mail.sender-name}")
    private String senderName;

    public void sendVoucherEmail(
            UserEmailRequest request
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
    max-width:650px;
    margin:auto;
    background:#ffffff;
    border-radius:24px;
    overflow:hidden;
    box-shadow:0 8px 24px rgba(0,0,0,0.08);
">

    <div style="
        background:linear-gradient(135deg,#0E4A63,#108ea2);
        padding:35px;
        text-align:center;
    ">

        %s

        <h1 style="
            color:white;
            margin-top:15px;
            margin-bottom:0;
            font-size:30px;
        ">
            Your Gift Voucher
        </h1>

    </div>

    <div style="padding:35px;">

        <p style="
            font-size:16px;
            color:#444;
            line-height:1.7;
        ">
            Hello %s,
            your voucher details are below.
        </p>

        <div style="
            background:#f8fafc;
            border-radius:18px;
            padding:28px;
            margin-top:25px;
        ">

            <p><b>Brand:</b> %s</p>

            <p><b>Amount:</b> ₹%s</p>

            <p><b>Voucher Code:</b> %s</p>

            <p><b>PIN:</b> %s</p>

            <p><b>Validity:</b> %s</p>

            <p>
                <b>Website:</b>
                <a href="%s" target="_blank">
                    Visit Brand Website
                </a>
            </p>

        </div>

        <div style="
            margin-top:35px;
            background:#eef7fb;
            padding:25px;
            border-radius:18px;
        ">

            <h2 style="
                margin-top:0;
                color:#0E4A63;
            ">
                How To Redeem
            </h2>

            <p style="
                color:#555;
                line-height:1.9;
                white-space:pre-line;
            ">
                %s
            </p>

        </div>

        <div style="
            margin-top:35px;
            text-align:center;
            color:#888;
            font-size:13px;
        ">
            Powered by TruVish
        </div>

    </div>

</div>

</body>
</html>
"""
                    .formatted(

                            request.getBrandLogo() != null
                                    && !request.getBrandLogo().isBlank()

                                    ? """
<img src="%s"
style="
width:120px;
height:auto;
border-radius:14px;
background:white;
padding:10px;
"/>
"""
                                    .formatted(
                                            request.getBrandLogo()
                                    )

                                    : "",

                            safe(request.getName()),
                            safe(request.getBrandName()),

                            request.getAmount() == null
                                    ? "0"
                                    : request.getAmount(),

                            safe(request.getVoucherCode()),
                            safe(request.getPin()),
                            safe(request.getValidityTill()),
                            safe(request.getBrandUrl()),
                            safe(request.getRedemptionProcess())
                    );

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from(
                                    senderName
                                            + " <"
                                            + senderEmail
                                            + ">"
                            )
                            .to(request.getEmail())
                            .subject("Your Gift Voucher")
                            .html(html)
                            .build();

            resend.emails().send(params);

            System.out.println(
                    "VOUCHER EMAIL SENT"
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "FAILED TO SEND EMAIL : "
                            + e.getMessage()
            );
        }
    }

    private String safe(String value) {

        return value == null
                || value.isBlank()

                ? "Not provided"

                : value.trim();
    }
}