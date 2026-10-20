package com.redislabs.university.RU102J.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebConfiguration implements WebMvcConfigurer {

    // Replaces the per-response "Access-Control-Allow-Origin: *" header the
    // Dropwizard resources used to add by hand (lets `npm run dev` on :8080 call the API).
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST");
    }
}
