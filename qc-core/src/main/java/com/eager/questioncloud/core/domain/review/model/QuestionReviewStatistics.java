package com.eager.questioncloud.core.domain.review.model;

import com.eager.questioncloud.core.domain.review.event.UpdateReviewStatisticsEvent;
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

    public void update(UpdateReviewStatisticsEvent updateReviewStatisticsEvent) {
        switch (updateReviewStatisticsEvent.getUpdateReviewType()) {
            case REGISTER -> updateByNewReview(updateReviewStatisticsEvent.getVarianceRate());
            case MODIFY -> updateByModifyReview(updateReviewStatisticsEvent.getVarianceRate());
            case DELETE -> updateByDeleteReview(updateReviewStatisticsEvent.getVarianceRate());
        }
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
