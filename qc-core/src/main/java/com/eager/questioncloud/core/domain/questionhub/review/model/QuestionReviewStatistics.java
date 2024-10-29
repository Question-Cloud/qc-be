package com.eager.questioncloud.core.domain.questionhub.review.model;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionReviewStatistics {
    private Long questionId;
    private int reviewCount;
    private int totalRate;
    private double averageRate;

    @Builder
    public QuestionReviewStatistics(Long questionId, int reviewCount, int totalRate, double averageRate) {
        this.questionId = questionId;
        this.reviewCount = reviewCount;
        this.totalRate = totalRate;
        this.averageRate = averageRate;
    }

    public static QuestionReviewStatistics create(Long questionId) {
        return QuestionReviewStatistics.builder()
            .questionId(questionId)
            .reviewCount(0)
            .totalRate(0)
            .averageRate(0.0)
            .build();
    }

    public void updateByNewReview(int newRate) {
        this.reviewCount = this.reviewCount + 1;
        this.totalRate = this.totalRate + newRate;
        double value = (double) totalRate / (double) reviewCount;
        this.averageRate = Math.round(value * 10.0) / 10.0;
    }

    public void updateByModifyReview(int fluctuationRate) {
        this.totalRate = this.totalRate + fluctuationRate;
        double value = (double) totalRate / (double) reviewCount;
        this.averageRate = Math.round(value * 10.0) / 10.0;
    }

    public void updateByDeleteReview(int rate) {
        this.reviewCount = this.reviewCount - 1;
        this.totalRate = this.totalRate - rate;
        double value = (double) totalRate / (double) reviewCount;
        this.averageRate = Math.round(value * 10.0) / 10.0;
    }
}
