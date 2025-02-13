package com.example.stroketest.controller;

import com.example.stroketest.model.TestResult;
import com.example.stroketest.service.StrokeTestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
public class StrokeTestController {

    private final StrokeTestService strokeTestService;

    public StrokeTestController(StrokeTestService strokeTestService) {
        this.strokeTestService = strokeTestService;
    }

    @PostMapping("/reaction-time")
    public ResponseEntity<TestResult> saveReactionTimeTest(@RequestBody TestResult testResult) {
        TestResult savedResult = strokeTestService.saveTestResult(testResult);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/{testId}")
    public ResponseEntity<TestResult> getTestResult(@PathVariable Long testId) {
        Optional<TestResult> result = strokeTestService.getTestResult(testId);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
