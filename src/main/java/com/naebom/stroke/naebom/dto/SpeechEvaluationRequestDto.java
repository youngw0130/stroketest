package com.naebom.stroke.naebom.dto;

public class SpeechEvaluationRequestDto {
    private String expectedText;
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
