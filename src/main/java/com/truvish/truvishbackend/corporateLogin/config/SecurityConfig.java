package com.truvish.truvishbackend.corporateLogin.config;

import com.truvish.truvishbackend.corporateLogin.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =================================================
                // CSRF
                // =================================================

                .csrf(csrf ->
                        csrf.disable()
                )

                // =================================================
                // CORS
                // =================================================
                // CORS configuration is handled by WebConfig.java
                // =================================================

                .cors(cors -> {
                })

                // =================================================
                // SESSION
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =================================================
                // AUTHORIZATION
                // =================================================

                .authorizeHttpRequests(auth -> auth

                        // =================================================
                        // CORS PREFLIGHT
                        // =================================================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // =================================================
                        // BOOK DEMO
                        // =================================================

                        .requestMatchers(
                                "/api/book-demo"
                        ).permitAll()

                        // =================================================
                        // OLD DEMO
                        // =================================================

                        .requestMatchers(
                                "/api/demo-requests"
                        ).permitAll()

                        // =================================================
                        // CORPORATE LOGIN
                        // =================================================

                        .requestMatchers(
                                "/api/corporate/login/**"
                        ).permitAll()

                        // =================================================
                        // CORPORATE DASHBOARD
                        // =================================================

                        .requestMatchers(
                                "/api/corporate/dashboard/**"
                        ).permitAll()

                        // =================================================
                        // CODE REPORT
                        // =================================================

                        .requestMatchers(
                                "/api/corporate/code-report/**"
                        ).permitAll()

                        // =================================================
                        // CLIENTS
                        // =================================================

                        .requestMatchers(
                                "/api/clients/**"
                        ).permitAll()

                        // =================================================
                        // REDEMPTION HISTORY
                        // =================================================

                        .requestMatchers(
                                "/api/redemption-history/**"
                        ).permitAll()

                        // =================================================
                        // TRUVISH
                        // =================================================

                        .requestMatchers(
                                "/api/truvish/**"
                        ).permitAll()

                        // =================================================
                        // THEMES
                        // =================================================

                        .requestMatchers(
                                "/api/themes/**"
                        ).permitAll()

                        // =================================================
                        // ADMIN
                        // =================================================

                        .requestMatchers(
                                "/api/admin/**"
                        ).permitAll()

                        // =================================================
                        // CLIENT BRAND
                        // =================================================

                        .requestMatchers(
                                "/api/client-choose-brand/**"
                        ).permitAll()

                        // =================================================
                        // INVENTORY
                        // =================================================

                        .requestMatchers(
                                "/api/inventory/**"
                        ).permitAll()

                        // =================================================
                        // VOUCHER INVENTORY
                        // =================================================

                        .requestMatchers(
                                "/api/voucher-inventory/**"
                        ).permitAll()

                        // =================================================
                        // REDEEM
                        // =================================================

                        .requestMatchers(
                                "/api/redeem/**"
                        ).permitAll()

                        // =================================================
                        // WALLET
                        // =================================================

                        .requestMatchers(
                                "/api/wallet/**"
                        ).permitAll()

                        // =================================================
                        // TRUCARD
                        // =================================================

                        .requestMatchers(
                                "/api/trucard/**"
                        ).permitAll()

                        // =================================================
                        // TRUCARD CODES
                        // =================================================

                        .requestMatchers(
                                "/api/trucard-codes/**"
                        ).permitAll()

                        // =================================================
                        // TRU BLANK CODE
                        // =================================================

                        .requestMatchers(
                                "/api/admin/tru-blank-code/**"
                        ).permitAll()

                        // =================================================
                        // UPLOADS
                        // =================================================

                        .requestMatchers(
                                "/uploads/**"
                        ).permitAll()

                        // =================================================
                        // EVERYTHING ELSE
                        // =================================================

                        .anyRequest().authenticated()
                )

                // =================================================
                // AUTHENTICATION PROVIDER
                // =================================================

                .authenticationProvider(
                        authenticationProvider
                )

                // =================================================
                // JWT FILTER
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // =================================================
                // BASIC AUTH DISABLED
                // =================================================

                .httpBasic(
                        httpBasic ->
                                httpBasic.disable()
                )

                // =================================================
                // FORM LOGIN DISABLED
                // =================================================

                .formLogin(
                        form ->
                                form.disable()
                );

        return http.build();
    }
}
