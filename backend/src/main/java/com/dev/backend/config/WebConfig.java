package com.dev.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/users/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);

    registry.addMapping("/posts/**")
            .allowedOrigins("http://localhost:4200")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    String imageDir = System.getProperty("user.dir") + "/uploads/images/";
    String videoDir = System.getProperty("user.dir") + "/uploads/videos/";

    registry.addResourceHandler("/images/**")
            .addResourceLocations("file:" + imageDir);

    registry.addResourceHandler("/videos/**")
            .addResourceLocations("file:" + videoDir);
    }
}

