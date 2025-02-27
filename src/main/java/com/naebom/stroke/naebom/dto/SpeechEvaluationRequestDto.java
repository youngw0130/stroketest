package com.naebom.stroke.naebom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpeechEvaluationRequestDto {
    @JsonProperty("expectedText")
    private String expectedText;
    @JsonProperty("base64Audio")
    private String base64Audio;

    public SpeechEvaluationRequestDto() {}

    public SpeechEvaluationRequestDto(String expectedText, String base64Audio) {
        this.expectedText = expectedText;
        this.base64Audio = base64Audio;
    }

    public String getExpectedText() {
        return expectedText;
    }

    public String getBase64Audio() {
        return base64Audio;
    }
}
