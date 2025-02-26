package com.naebom.stroke.naebom.service;

import com.microsoft.cognitiveservices.speech.*;
import com.naebom.stroke.naebom.utils.LevenshteinUtil;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SpeechService {

    private final SpeechConfig speechConfig;

    public SpeechService(SpeechConfig speechConfig) {
        this.speechConfig = speechConfig;
    }

    public double processAudio(String base64Audio, String originalText) throws Exception {
        String filePath = "temp_audio.wav";

        // Base64 데이터를 디코딩하여 WAV 파일로 저장
        byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
        Files.write(Paths.get(filePath), audioBytes);

        // 음성을 텍스트로 변환
        String transcribedText = transcribeAudio(filePath);

        // 변환된 텍스트와 원본 텍스트 비교
        return calculateAccuracy(transcribedText, originalText);
    }

    private String transcribeAudio(String filePath) throws Exception {
        SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, AudioConfig.fromWavFileInput(filePath));
        Future<SpeechRecognitionResult> resultFuture = recognizer.recognizeOnceAsync();
        SpeechRecognitionResult result = resultFuture.get();
        recognizer.close();

        return result.getText(); // 변환된 텍스트 반환
    }

    private double calculateAccuracy(String transcribedText, String originalText) {
        int distance = LevenshteinUtil.levenshteinDistance(transcribedText, originalText);
        int maxLength = Math.max(transcribedText.length(), originalText.length());
        return (1 - (double) distance / maxLength) * 100; // 퍼센트(%) 값 반환
    }
}
