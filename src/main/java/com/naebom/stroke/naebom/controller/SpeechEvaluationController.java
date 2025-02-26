package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import com.naebom.stroke.naebom.dto.SpeechEvaluationResponseDto;
import com.naebom.stroke.naebom.service.SpeechService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speech")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SpeechController {

    private final SpeechService speechService;

    public SpeechController(SpeechService speechService) {
        this.speechService = speechService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<SpeechEvaluationResponseDto> evaluateSpeech(@RequestBody SpeechEvaluationRequestDto request) {
        try {
            double accuracy = speechService.processAudio(request.getBase64Audio(), request.getOriginalText());
            return ResponseEntity.ok(new SpeechEvaluationResponseDto(accuracy));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SpeechEvaluationResponseDto(0.0));
        }
    }
}
