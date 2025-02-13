package com.example.stroketest.service;

import com.example.stroketest.model.TestResult;
import com.example.stroketest.repository.TestResultRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StrokeTestService {

    private final TestResultRepository testResultRepository;

    public StrokeTestService(TestResultRepository testResultRepository) {
        this.testResultRepository = testResultRepository;
    }

    public TestResult saveTestResult(TestResult testResult) {
        return testResultRepository.save(testResult);
    }

    public Optional<TestResult> getTestResult(Long testId) {
        return testResultRepository.findById(testId);
    }
}
