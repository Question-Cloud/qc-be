package com.eager.questioncloud.core.domain.creator.model;

import lombok.Getter;

@Getter
public class CreatorStatistics {
    private Long creatorId;
    private int subscribeCount;
    private int saleCount;
    private int reviewCount;
    private int totalReviewRate;
    private Double averageRateOfReview;

    public CreatorStatistics(Long creatorId, int subscribeCount, int saleCount, int reviewCount, int totalReviewRate, Double averageRateOfReview) {
        this.creatorId = creatorId;
        this.subscribeCount = subscribeCount;
        this.saleCount = saleCount;
        this.reviewCount = reviewCount;
        this.totalReviewRate = totalReviewRate;
        this.averageRateOfReview = averageRateOfReview;
    }

    public static CreatorStatistics create(Long creatorId) {
        return new CreatorStatistics(creatorId, 0, 0, 0, 0, 0.0);
    }

    public void addSaleCount(int count) {
        this.saleCount += count;
    }

    public void increaseSubscribeCount() {
        this.subscribeCount += 1;
    }

    public void decreaseSubscribeCount() {
        this.subscribeCount -= 1;
    }
}
