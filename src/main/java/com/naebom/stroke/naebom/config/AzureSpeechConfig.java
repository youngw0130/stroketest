
package com.naebom.stroke.naebom.config;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureSpeechConfig {

    @Value("${azure.speech.key}")
    private String speechKey;

    @Value("${azure.speech.region}")
    private String speechRegion;

    @Bean
    public SpeechConfig speechConfig() {
        return SpeechConfig.fromSubscription(speechKey, speechRegion);
    }
}

