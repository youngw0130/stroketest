package com.example.stroketest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double reactionTime;
    private boolean facialParalysis;
    private boolean speechImpairment;
    private double strokeProbability;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getReactionTime() {
        return reactionTime;
    }

    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    public boolean isFacialParalysis() {
        return facialParalysis;
    }

    public void setFacialParalysis(boolean facialParalysis) {
        this.facialParalysis = facialParalysis;
    }

    public boolean isSpeechImpairment() {
        return speechImpairment;
    }

    public void setSpeechImpairment(boolean speechImpairment) {
        this.speechImpairment = speechImpairment;
    }

    public double getStrokeProbability() {
        return strokeProbability;
    }

    public void setStrokeProbability(double strokeProbability) {
        this.strokeProbability = strokeProbability;
    }
}
