package com.eager.questioncloud.core.domain.creator.model;

import lombok.Getter;

@Getter
public class CreatorStatistics {
    private Long creatorId;
    private int subscribeCount;
    private int salesCount;
    private int reviewCount;
    private int totalReviewRate;
    private Double averageRateOfReview;

    public CreatorStatistics(Long creatorId, int subscribeCount, int salesCount, int reviewCount, int totalReviewRate, Double averageRateOfReview) {
        this.creatorId = creatorId;
        this.subscribeCount = subscribeCount;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.totalReviewRate = totalReviewRate;
        this.averageRateOfReview = averageRateOfReview;
    }

    public static CreatorStatistics create(Long creatorId) {
        return new CreatorStatistics(creatorId, 0, 0, 0, 0, 0.0);
    }

    public void addSaleCount(int count) {
        this.salesCount += count;
    }

    public void increaseSubscribeCount() {
        this.subscribeCount += 1;
    }

    public void decreaseSubscribeCount() {
        this.subscribeCount -= 1;
    }

    public void updateReviewStatisticsByRegisteredReview(int newRate) {
        this.reviewCount = this.reviewCount + 1;
        this.totalReviewRate = this.totalReviewRate + newRate;
        double value = (double) totalReviewRate / (double) reviewCount;
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0;
    }

    public void updateReviewStatisticsByModifiedReview(int varianceRate) {
        this.totalReviewRate = this.totalReviewRate + varianceRate;
        double value = (double) totalReviewRate / (double) reviewCount;
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0;
    }

    public void updateReviewStatisticsByDeletedReview(int rate) {
        this.reviewCount = this.reviewCount - 1;
        this.totalReviewRate = this.totalReviewRate - rate;
        double value = (double) totalReviewRate / (double) reviewCount;
        this.averageRateOfReview = Math.round(value * 10.0) / 10.0;
    }
}
