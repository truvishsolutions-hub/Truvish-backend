package com.truvish.truvishbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // =========================================================
    // UPLOAD DIRECTORY
    // =========================================================

    private static final String UPLOAD_DIR = "uploads/";

    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Override
    public void addCorsMappings(
            CorsRegistry registry
    ) {

        registry.addMapping("/**")

                // =================================================
                // FRONTEND DOMAINS
                // =================================================

                .allowedOriginPatterns(

                        // MAIN DOMAIN
                        "https://truvish.com",
                        "https://www.truvish.com",

                        // RAILWAY
                        "https://*.up.railway.app",

                        // NETLIFY
                        "https://*.netlify.app",

                        // CLOUDFLARE
                        "https://*.trycloudflare.com",

                        // OTHER
                        "https://trivish-redeem.com",
                        "https://client-request-production.up.railway.app",

                        // LOCALHOST
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "http://localhost:5176"
                )

                // =================================================
                // METHODS
                // =================================================

                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )

                // =================================================
                // HEADERS
                // =================================================

                .allowedHeaders("*")

                // =================================================
                // EXPOSE HEADERS
                // =================================================

                .exposedHeaders(
                        HttpHeaders.AUTHORIZATION,
                        HttpHeaders.CONTENT_TYPE
                )

                // =================================================
                // COOKIES / TOKENS
                // =================================================

                .allowCredentials(true)

                // =================================================
                // PREFLIGHT CACHE
                // =================================================

                .maxAge(3600);
    }

    // =========================================================
    // STATIC FILE ACCESS
    // =========================================================

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        registry.addResourceHandler(
                        "/uploads/**"
                )
                .addResourceLocations(
                        "file:" + UPLOAD_DIR
                );
    }
}