package com.eager.questioncloud.core.domain.review;

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics;
import lombok.Builder;

@Builder
public class QuestionReviewStatisticsBuilder {
    @Builder.Default
    private Long questionId = 1L;
    @Builder.Default
    private int reviewCount = 0;
    @Builder.Default
    private int totalRate = 0;
    @Builder.Default
    private double averageRate = 0.0;

    public QuestionReviewStatistics toQuestionReviewStatistics() {
        return QuestionReviewStatistics.builder()
            .questionId(questionId)
            .reviewCount(reviewCount)
            .totalRate(totalRate)
            .averageRate(averageRate)
            .build();
    }
}
