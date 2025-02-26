package com.naebom.stroke.naebom.dto;

public class SpeechEvaluationResponseDto {
    private double score;

    public SpeechEvaluationResponseDto(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
