package com.truvish.truvishbackend;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatMultipartConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            connector.setMaxParameterCount(20000);    // Fix for many form parts
            connector.setMaxPostSize(200 * 1024 * 1024);   // 200MB
            connector.setMaxSavePostSize(200 * 1024 * 1024);
        });
    }
}
