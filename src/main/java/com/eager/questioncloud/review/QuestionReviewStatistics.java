package com.eager.questioncloud.review;

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

    public QuestionReviewStatisticsEntity toEntity() {
        return QuestionReviewStatisticsEntity.builder()
            .questionId(questionId)
            .reviewCount(reviewCount)
            .totalRate(totalRate)
            .averageRate(averageRate)
            .build();
    }
}
