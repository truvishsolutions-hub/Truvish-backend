package com.truvish.truvishbackend.ClientEmailService;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

@Service
public class ClientEmailService {

    // =========================================================
    // LOGGER
    // =========================================================
    private static final Logger logger =
            LoggerFactory.getLogger(
                    ClientEmailService.class
            );

    // =========================================================
    // RESEND API KEY
    // =========================================================
    @Value("${resend.api.key}")
    private String resendApiKey;

    // =========================================================
    // SENDER
    // =========================================================
    @Value("${app.mail.sender-name}")
    private String senderName;

    @Value("${app.mail.sender-email}")
    private String senderEmail;

    // =========================================================
    // BACKEND URL
    // =========================================================
    @Value("${app.backend.url:http://localhost:8080}")
    private String backendUrl;

    // =========================================================
    // SEND VOUCHER EMAIL
    // =========================================================
    public void sendVoucher(
            String toEmail,
            String name,
            String voucherCode,
            String clientLogo
    ) {

        try {

            // =================================================
            // DEFAULT NAME
            // =================================================
            if (name == null || name.trim().isEmpty()) {
                name = "There";
            }

            // =================================================
            // DEFAULT VOUCHER
            // =================================================
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                voucherCode = "TRUVISH-CODE";
            }

            // =================================================
            // CLIENT LOGO HTML
            // =================================================
            String clientLogoHtml = "";

            if (
                    clientLogo != null &&
                            !clientLogo.trim().isEmpty()
            ) {

                // =============================================
                // FULL IMAGE URL
                // =============================================
                String logoUrl;

                if (
                        clientLogo.startsWith("http://") ||
                                clientLogo.startsWith("https://")
                ) {

                    logoUrl = clientLogo;

                } else {

                    logoUrl =
                            backendUrl +
                                    clientLogo;
                }

                clientLogoHtml = """

<img
    src="%s"
    width="80"
    height="80"
    alt="Client Logo"
    style="
       border-radius:14px;
       object-fit:cover;
       background:white;
       padding:6px;
       border:2px solid rgba(255,255,255,0.3);
       display:block;
    "
/>

""".formatted(logoUrl);

                logger.info(
                        "Client logo URL added: {}",
                        logoUrl
                );
            }

            // =================================================
            // TRUVISH LOGO URL
            // =================================================
            String truvishLogoUrl =
                    backendUrl + "/TV-BG.png";

            // =================================================
            // HTML
            // =================================================
            String html = """

<!DOCTYPE html>
<html>

<body style="
    margin:0;
    padding:0;
    background:#F3F7FA;
    font-family:Arial,sans-serif;
">

<table width="100%%"
       cellpadding="0"
       cellspacing="0"
       style="
          background:#F3F7FA;
          padding:40px 15px;
">

<tr>

<td align="center">

<table width="100%%"
       cellpadding="0"
       cellspacing="0"
       style="
          max-width:650px;
          background:#ffffff;
          border-radius:24px;
          overflow:hidden;
          box-shadow:0 10px 35px rgba(0,0,0,0.08);
">

<!-- TOP -->
<tr>

<td style="
    background:linear-gradient(135deg,#0E4A63,#1AB0B7);
    padding:40px 25px;
">

<table width="100%%">

<tr>

<td align="left">

%s

</td>

<td align="right">

<img
    src="%s"
    width="130"
    alt="Truvish Logo"
/>

</td>

</tr>

</table>

<div style="
    text-align:center;
    margin-top:30px;
">

<h1 style="
    color:white;
    margin:0;
    font-size:34px;
    font-weight:700;
">

Congratulations 🎉

</h1>

<p style="
    color:rgba(255,255,255,0.92);
    margin-top:14px;
    font-size:16px;
    line-height:1.7;
">

Your reward voucher is ready

</p>

</div>

</td>

</tr>

<!-- BODY -->
<tr>

<td style="
    padding:40px 34px;
">

<p style="
    margin:0;
    font-size:18px;
    color:#1D2E39;
    line-height:1.8;
">

Hello <b>%s</b>,

</p>

<p style="
    margin-top:22px;
    font-size:16px;
    color:#4B5563;
    line-height:1.9;
">

You have received a reward voucher
from <b>Truvish Solutions</b>.

<br><br>

Use the voucher code below to redeem
your rewards instantly.

</p>

<!-- VOUCHER -->
<div style="
    margin-top:34px;
    background:linear-gradient(135deg,#0E4A63,#1AB0B7);
    border-radius:22px;
    padding:36px 20px;
    text-align:center;
    box-shadow:0 10px 30px rgba(26,176,183,0.25);
">

<p style="
    color:rgba(255,255,255,0.85);
    font-size:13px;
    letter-spacing:1px;
    margin-bottom:16px;
">

YOUR VOUCHER CODE

</p>

<h2 style="
    color:white;
    margin:0;
    font-size:34px;
    letter-spacing:4px;
    font-weight:700;
">

%s

</h2>

</div>

<!-- BUTTON -->
<div style="
    margin-top:36px;
    text-align:center;
">

<a href="https://truvish.com"
   style="
      display:inline-block;
      background:#1AB0B7;
      color:white;
      text-decoration:none;
      padding:16px 34px;
      border-radius:14px;
      font-size:16px;
      font-weight:700;
   ">

Redeem Now

</a>

</div>

<!-- FOOTER -->
<div style="
    margin-top:45px;
    padding-top:24px;
    border-top:1px solid #E5E7EB;
    text-align:center;
">

<img
    src="%s"
    width="110"
    alt="Truvish Footer Logo"
    style="margin-bottom:14px;"
/>

<p style="
    color:#6B7280;
    font-size:14px;
    line-height:1.8;
    margin:0;
">

Thank you for choosing
<b>Truvish Solutions</b>.

</p>

<p style="
    margin-top:12px;
    color:#9CA3AF;
    font-size:12px;
    line-height:1.7;
">

This email was sent automatically.
Please do not reply to this email.

</p>

</div>

</td>

</tr>

</table>

</td>

</tr>

</table>

</body>

</html>

""".formatted(
                    clientLogoHtml,
                    truvishLogoUrl,
                    name,
                    voucherCode,
                    truvishLogoUrl
            );

            // =================================================
            // SEND VIA RESEND
            // =================================================
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params =
                    CreateEmailOptions.builder()
                            .from(
                                    senderName
                                            + " <"
                                            + senderEmail
                                            + ">"
                            )
                            .to(toEmail)
                            .subject(
                                    "Congratulations! Your Reward is Here 🎉"
                            )
                            .html(html)
                            .build();

            resend.emails().send(params);

            logger.info(
                    "Voucher email sent successfully to {}",
                    toEmail
            );

        } catch (Exception e) {

            logger.error(
                    "Failed to send voucher email",
                    e
            );

            throw new RuntimeException(
                    "Failed to send email"
            );
        }
    }
}