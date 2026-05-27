package com.truvish.truvishbackend.ClientEmailService;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientEmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(ClientEmailService.class);

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${app.backend.url}")
    private String backendUrl;

    public void sendVoucher(
            String toEmail,
            String name,
            String voucherCode,
            String clientLogo,
            Integer validityDays,
            String companyName
    ) {

        try {

            // =========================
            // DEFAULT VALUES
            // =========================

            if (name == null || name.isBlank()) {
                name = "Customer";
            }

            if (voucherCode == null || voucherCode.isBlank()) {
                voucherCode = "TRUVISH-CODE";
            }

            if (validityDays == null || validityDays <= 0) {
                validityDays = 60;
            }

            if (companyName == null || companyName.isBlank()) {
                companyName = "TruVish";
            }

            String validityText =
                    validityDays + (validityDays == 1 ? " Month" : " Months");

            // =========================
            // LOGO URL
            // =========================

            String logoUrl = "";

            if (clientLogo != null && !clientLogo.isBlank()) {

                String cacheBuster = "?t=" + System.currentTimeMillis();

                if (clientLogo.startsWith("http://")
                        || clientLogo.startsWith("https://")) {

                    logoUrl = clientLogo + cacheBuster;

                } else {

                    clientLogo = clientLogo.replaceFirst("^/+", "");

                    logoUrl =
                            backendUrl + "/" + clientLogo + cacheBuster;
                }
            }

            logger.info("LOGO URL : {}", logoUrl);

            // =========================
            // CLIENT LOGO HTML
            // =========================

            String clientLogoHtml = "";

            if (!logoUrl.isBlank()) {

                clientLogoHtml = """
                    <img
                        src="%s"
                        alt="Client Logo"
                        style="
                            width:110px;
                            height:110px;
                            object-fit:cover;
                            border-radius:24px;
                            background:#ffffff;
                            padding:12px;
                            display:block;
                            margin:auto;
                            box-shadow:0 8px 20px rgba(0,0,0,0.15);
                        "
                    />
                    """.formatted(logoUrl);
            }

            // =========================
            // EMAIL HTML
            // =========================

            String html = """
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>

<style>

body{
    margin:0;
    padding:0;
    background:#F3F7FA;
    font-family:Arial,sans-serif;
}

.wrapper{
    width:100%%;
    padding:30px 12px;
    box-sizing:border-box;
}

.card{
    max-width:640px;
    margin:auto;
    background:#ffffff;
    border-radius:28px;
    overflow:hidden;
    box-shadow:0 12px 35px rgba(14,74,99,0.12);
}

.top{
    background:linear-gradient(135deg,#0E4A63,#1AB0B7);
    padding:45px 20px;
    text-align:center;
}

.content{
    padding:40px 28px;
    text-align:center;
}

.heading{
    font-size:34px;
    font-weight:800;
    color:#0E4A63;
    margin-bottom:16px;
}

.subtext{
    font-size:16px;
    line-height:1.9;
    color:#1D2E39;
    margin-top:15px;
}

.voucher-box{
    margin-top:35px;
    background:linear-gradient(135deg,#F3F7FA,#EAF7F8);
    border:2px dashed #1AB0B7;
    border-radius:22px;
    padding:30px 20px;
}

.voucher-label{
    font-size:13px;
    color:#0E4A63;
    letter-spacing:2px;
    margin-bottom:16px;
    font-weight:700;
}

.voucher-code{
    font-size:32px;
    font-weight:800;
    color:#0E4A63;
    letter-spacing:4px;
    word-break:break-word;
}

.validity-box{
    margin-top:24px;
    background:#EAF7F8;
    border-radius:16px;
    padding:16px;
}

.validity-text{
    color:#0E4A63;
    font-size:15px;
    font-weight:700;
}

.redeem-btn{
    display:inline-block;
    margin-top:32px;
    background:linear-gradient(135deg,#0E4A63,#1AB0B7);
    color:#ffffff !important;
    text-decoration:none;
    padding:16px 34px;
    border-radius:16px;
    font-size:15px;
    font-weight:700;
    letter-spacing:0.5px;
    box-shadow:0 10px 20px rgba(26,176,183,0.25);
}

.section{
    margin-top:35px;
    background:#F8FBFC;
    border-radius:20px;
    padding:26px;
    text-align:left;
    border:1px solid #E1EEF2;
}

.section h3{
    margin-top:0;
    margin-bottom:18px;
    color:#0E4A63;
    font-size:21px;
}

.section p{
    margin:12px 0;
    line-height:1.8;
    color:#1D2E39;
    font-size:15px;
}

.footer{
    background:#0E4A63;
    padding:32px 20px;
    text-align:center;
}

.footer-title{
    color:#ffffff;
    font-size:18px;
    font-weight:700;
}

.footer-text{
    margin-top:12px;
    color:#D9EDF2;
    font-size:13px;
    line-height:1.8;
}

.highlight{
    color:#1AB0B7;
    font-weight:700;
}

@media only screen and (max-width:600px){

    .content{
        padding:32px 18px;
    }

    .heading{
        font-size:28px;
    }

    .voucher-code{
        font-size:24px;
        letter-spacing:2px;
    }

    .section{
        padding:22px;
    }

    .redeem-btn{
        width:100%%;
        box-sizing:border-box;
    }
}

</style>

</head>

<body>

<div class="wrapper">

<div class="card">

    <!-- TOP HEADER -->
    <div class="top">

        %s

    </div>

    <!-- CONTENT -->
    <div class="content">

        <div class="heading">
            Congratulations 🎉
        </div>

        <div class="subtext">

            Hey <b>%s</b>,<br/><br/>

            <b>%s</b> has sent you an exclusive reward voucher 🎁<br/>

            Follow the instructions below and redeem your
            favourite gift vouchers using your unique code.

        </div>

        <!-- VOUCHER -->
        <div class="voucher-box">

            <div class="voucher-label">
                YOUR UNIQUE VOUCHER CODE
            </div>

            <div class="voucher-code">
                %s
            </div>

        </div>

        <!-- VALIDITY -->
        <div class="validity-box">

            <div class="validity-text">
                📅 Valid For %s
            </div>

        </div>

        <!-- BUTTON -->
        <a
            href="https://redeem.truvish.com/"
            class="redeem-btn"
        >
            REDEEM NOW
        </a>

        <!-- HOW TO REDEEM -->
        <div class="section">

            <h3>How To Redeem</h3>

            <p>1. Open TruVish website or mobile app.</p>

            <p>2. Login to your TruVish account.</p>

            <p>3. Open the Redeem Voucher section.</p>

            <p>4. Enter your voucher code.</p>

            <p>5. Enjoy your rewards instantly.</p>

        </div>

        <!-- TERMS -->
        <div class="section">

            <h3>Terms & Conditions</h3>

            <p>• Voucher valid for limited time only.</p>

            <p>• Voucher can be redeemed once only.</p>

            <p>• Voucher cannot be exchanged for cash.</p>

            <p>• TruVish reserves the right to modify or cancel offers anytime.</p>

        </div>

    </div>

    <!-- FOOTER -->
    <div class="footer">

        <div class="footer-title">
            TruVish Rewards
        </div>

        <div class="footer-text">

            Rewards • Loyalty • Gift Vouchers<br/>

            <span class="highlight">
                Trusted Digital Reward Platform
            </span>

        </div>

    </div>

</div>

</div>

</body>
</html>
""".formatted(
                    clientLogoHtml,
                    name,
                    companyName,
                    voucherCode,
                    validityText
            );

            // =========================
            // RESEND
            // =========================

            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions request =
                    CreateEmailOptions.builder()
                            .from("TruVish <noreply@truvish.com>")
                            .to(toEmail)
                            .subject("🎁 Your Reward Voucher from "
                                    + companyName)
                            .html(html)
                            .build();

            resend.emails().send(request);

            logger.info("EMAIL SENT SUCCESSFULLY TO {}", toEmail);

        } catch (Exception e) {

            logger.error("FAILED TO SEND EMAIL", e);

            throw new RuntimeException(
                    "Email sending failed : " + e.getMessage()
            );
        }
    }
}