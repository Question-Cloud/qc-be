package com.eager.questioncloud.core.domain.creator.model;

import lombok.Getter;

@Getter
public class CreatorStatistics {
    private Long creatorId;
    private int subscribeCount;
    private int saleCount;
    private Double averageRateOfReview;

    public CreatorStatistics(Long creatorId, int subscribeCount, int saleCount, Double averageRateOfReview) {
        this.creatorId = creatorId;
        this.subscribeCount = subscribeCount;
        this.saleCount = saleCount;
        this.averageRateOfReview = averageRateOfReview;
    }

    public static CreatorStatistics create(Long creatorId) {
        return new CreatorStatistics(creatorId, 0, 0, 0.0);
    }
}
