package com.naebom.stroke.naebom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean//외부 API는 Spring이 자동으로 등록X
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
