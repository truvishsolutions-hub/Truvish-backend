package com.truvish.truvishbackend.corporateLogin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Generate JWT Token
     */
    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + jwtExpiration
                        )
                )
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Extract Email From JWT
     */
    public String extractEmail(String token) {

        return extractAllClaims(token).getSubject();
    }

    /**
     * Validate Token Using Email
     */
    public boolean isTokenValid(
            String token,
            String email
    ) {

        String tokenEmail = extractEmail(token);

        return tokenEmail.equals(email)
                && !isTokenExpired(token);
    }

    /**
     * Validate Token Using UserDetails
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        String email = extractEmail(token);

        return email.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /**
     * Check Whether Token Is Expired
     */
    private boolean isTokenExpired(String token) {

        Date expirationDate =
                extractAllClaims(token).getExpiration();

        return expirationDate.before(new Date());
    }

    /**
     * Extract All Claims
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Secret Signing Key
     *
     * Removes spaces/newlines from Railway environment variable
     * before Base64 decoding.
     */
    private SecretKey getSignInKey() {

        String cleanSecret = secretKey
                .replaceAll("\\s+", "");

        byte[] keyBytes =
                Decoders.BASE64.decode(cleanSecret);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
