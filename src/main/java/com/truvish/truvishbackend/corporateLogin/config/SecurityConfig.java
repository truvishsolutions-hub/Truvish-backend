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

                .cors(cors ->
                        cors.configurationSource(
                                request -> {

                                    var configuration =
                                            new org.springframework.web.cors.CorsConfiguration();


                                    // ---------------------------------
                                    // ALLOWED ORIGINS
                                    // ---------------------------------

                                    configuration.setAllowedOriginPatterns(
                                            java.util.List.of(

                                                    // Production
                                                    "https://truvish.com",
                                                    "https://www.truvish.com",

                                                    // Railway
                                                    "https://*.up.railway.app",

                                                    // Netlify
                                                    "https://*.netlify.app",

                                                    // Cloudflare Tunnel
                                                    "https://*.trycloudflare.com",

                                                    // Redeem frontend
                                                    "https://trivish-redeem.com",

                                                    // Client request frontend/backend
                                                    "https://client-request-production.up.railway.app",

                                                    // Local development
                                                    "http://localhost:3000",
                                                    "http://localhost:5173",
                                                    "http://localhost:5174",
                                                    "http://localhost:5175",
                                                    "http://localhost:5176",
                                                    "http://localhost:5177"
                                            )
                                    );


                                    // ---------------------------------
                                    // METHODS
                                    // ---------------------------------

                                    configuration.setAllowedMethods(
                                            java.util.List.of(
                                                    "GET",
                                                    "POST",
                                                    "PUT",
                                                    "DELETE",
                                                    "PATCH",
                                                    "OPTIONS"
                                            )
                                    );


                                    // ---------------------------------
                                    // HEADERS
                                    // ---------------------------------

                                    configuration.setAllowedHeaders(
                                            java.util.List.of("*")
                                    );


                                    // ---------------------------------
                                    // EXPOSED HEADERS
                                    // ---------------------------------

                                    configuration.setExposedHeaders(
                                            java.util.List.of(
                                                    "Authorization",
                                                    "Content-Type"
                                            )
                                    );


                                    // ---------------------------------
                                    // CREDENTIALS
                                    // ---------------------------------

                                    configuration.setAllowCredentials(true);


                                    // ---------------------------------
                                    // CACHE
                                    // ---------------------------------

                                    configuration.setMaxAge(3600L);


                                    return configuration;
                                }
                        )
                )


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


                        // -----------------------------------------
                        // OPTIONS
                        // -----------------------------------------

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CORPORATE LOGIN
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/corporate/login/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CORPORATE DASHBOARD
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/corporate/dashboard/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CODE REPORT
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/corporate/code-report/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CLIENTS
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/clients/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CLIENT REDEMPTION HISTORY
                        //
                        // IMPORTANT:
                        // Frontend is calling:
                        // /api/redemption-history/client/20
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/redemption-history/**"
                        ).permitAll()


                        // -----------------------------------------
                        // TRUVISH HISTORY / APIs
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/truvish/**"
                        ).permitAll()


                        // -----------------------------------------
                        // THEMES
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/themes/**"
                        ).permitAll()


                        // -----------------------------------------
                        // ADMIN
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/admin/**"
                        ).permitAll()


                        // -----------------------------------------
                        // CLIENT BRAND
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/client-choose-brand/**"
                        ).permitAll()


                        // -----------------------------------------
                        // INVENTORY
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/inventory/**"
                        ).permitAll()


                        // -----------------------------------------
                        // VOUCHER INVENTORY
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/voucher-inventory/**"
                        ).permitAll()


                        // -----------------------------------------
                        // REDEEM
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/redeem/**"
                        ).permitAll()


                        // -----------------------------------------
                        // WALLET
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/wallet/**"
                        ).permitAll()


                        // -----------------------------------------
                        // TRUCARD
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/trucard/**"
                        ).permitAll()


                        // -----------------------------------------
                        // TRUCARD CODES
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/trucard-codes/**"
                        ).permitAll()


                        // -----------------------------------------
                        // TRU BLANK CODE
                        // -----------------------------------------

                        .requestMatchers(
                                "/api/admin/tru-blank-code/**"
                        ).permitAll()


                        // -----------------------------------------
                        // UPLOADS
                        // -----------------------------------------

                        .requestMatchers(
                                "/uploads/**"
                        ).permitAll()


                        // -----------------------------------------
                        // EVERYTHING ELSE
                        // -----------------------------------------

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