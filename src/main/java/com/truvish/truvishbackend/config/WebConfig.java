package com.truvish.truvishbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // =========================================================
    // UPLOAD DIRECTORY
    // =========================================================

    private static final String UPLOAD_DIR = "uploads/";

    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // =====================================================
        // ALLOWED ORIGINS
        // =====================================================

        configuration.setAllowedOriginPatterns(
                List.of(

                        // =================================================
                        // TRUVISH FRONTENDS
                        // =================================================

                        "https://truvish.com",
                        "https://www.truvish.com",
                        "https://client.truvish.com",
                        "https://admin.truvish.com",
                        "https://redeem.truvish.com",

                        // =================================================
                        // RAILWAY FRONTENDS / SERVICES
                        // =================================================

                        "https://*.up.railway.app",

                        // =================================================
                        // NETLIFY
                        // =================================================

                        "https://*.netlify.app",

                        // =================================================
                        // CLOUDFLARE
                        // =================================================

                        "https://*.trycloudflare.com",

                        // =================================================
                        // OTHER DOMAINS
                        // =================================================

                        "https://trivish-redeem.com",
                        "https://www.trivish-redeem.com",

                        // =================================================
                        // LOCAL DEVELOPMENT
                        // =================================================

                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "http://localhost:5176",
                        "http://localhost:5177"
                )
        );

        // =====================================================
        // HTTP METHODS
        // =====================================================

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS",
                        "HEAD"
                )
        );

        // =====================================================
        // REQUEST HEADERS
        // =====================================================

        configuration.setAllowedHeaders(
                List.of("*")
        );

        // =====================================================
        // EXPOSED HEADERS
        // =====================================================

        configuration.setExposedHeaders(
                List.of(
                        HttpHeaders.AUTHORIZATION,
                        HttpHeaders.CONTENT_TYPE
                )
        );

        // =====================================================
        // CREDENTIALS
        // =====================================================

        configuration.setAllowCredentials(true);

        // =====================================================
        // PREFLIGHT CACHE
        // =====================================================

        configuration.setMaxAge(3600L);

        // =====================================================
        // REGISTER CORS FOR ALL ENDPOINTS
        // =====================================================

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    // =========================================================
    // STATIC FILE ACCESS
    // =========================================================

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
    }
}
