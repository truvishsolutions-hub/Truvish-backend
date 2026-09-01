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

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .csrf(csrf -> csrf.disable())

                .cors(cors -> {
                })

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/clients/exists"
                        ).permitAll()

                        .requestMatchers(
                                "/api/book-demo",
                                "/api/demo-requests"
                        ).permitAll()

                        .requestMatchers(
                                "/api/corporate/login/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/corporate/dashboard/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/corporate/code-report/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/clients/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/redemption-history/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/truvish/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/themes/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/admin/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/client-choose-brand/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/inventory/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/voucher-inventory/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/redeem/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/wallet/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/trucard/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/trucard-codes/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/admin/tru-blank-code/**"
                        ).permitAll()

                        .requestMatchers(
                                "/uploads/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .authenticationProvider(
                        authenticationProvider
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                .httpBasic(
                        httpBasic -> httpBasic.disable()
                )

                .formLogin(
                        form -> form.disable()
                );

        return http.build();
    }
}
