
package com.naebom.stroke.naebom.controller;

import com.naebom.stroke.naebom.dto.SpeechEvaluationRequestDto;
import com.naebom.stroke.naebom.dto.SpeechEvaluationResponseDto;
import com.naebom.stroke.naebom.service.SpeechEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/speech")
@CrossOrigin(origins = "*")
public class SpeechEvaluationController {

    private final SpeechEvaluationService speechEvaluationService;

    public SpeechEvaluationController(SpeechEvaluationService speechEvaluationService) {
        this.speechEvaluationService = speechEvaluationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<SpeechEvaluationResponseDto> evaluateSpeech(@RequestBody SpeechEvaluationRequestDto request) {
        try {
            double score = speechEvaluationService.evaluateSpeech(request);
            return ResponseEntity.ok(new SpeechEvaluationResponseDto(score));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

