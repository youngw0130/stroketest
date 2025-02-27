
package com.naebom.stroke.naebom.service;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class SpeechEvaluationService {

    private final SpeechConfig speechConfig;

    public SpeechEvaluationService(SpeechConfig speechConfig) {
        this.speechConfig = speechConfig;
    }

    public double evaluateSpeech(SpeechEvaluationRequestDto request) throws Exception {
        byte[] audioBytes = Base64.getDecoder().decode(request.getBase64Audio());

        // 음성 인식 구성
        AudioConfig audioConfig = AudioConfig.fromWavFileInput("temp_audio.wav");
        PronunciationAssessmentConfig pronunciationConfig = new PronunciationAssessmentConfig(
                request.getExpectedText(),
                PronunciationAssessmentGradingSystem.HundredMark,
                PronunciationAssessmentGranularity.Phoneme);

        SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);
        pronunciationConfig.applyTo(recognizer);

        SpeechRecognitionResult result = recognizer.recognizeOnceAsync().get();

        if (result.getReason() == ResultReason.RecognizedSpeech) {
            return PronunciationAssessmentResult.fromResult(result).getPronunciationScore();
        } else {
            throw new RuntimeException("음성 인식 실패: " + result.getReason());
        }
    }
}

