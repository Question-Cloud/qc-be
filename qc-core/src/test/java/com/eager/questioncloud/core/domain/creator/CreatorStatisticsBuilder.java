package com.eager.questioncloud.core.domain.creator;

import lombok.Builder;

@Builder
public class CreatorStatisticsBuilder {
    @Builder.Default
    private Long creatorId = 1L;
    @Builder.Default
    private int subscribeCount = 100;
    @Builder.Default
    private int salesCount = 100;
    @Builder.Default
    private int reviewCount = 100;
    @Builder.Default
    private int totalReviewRate = 100;
    @Builder.Default
    private Double averageRateOfReview = 1.0;

    public CreatorStatistics toCreatorStatistics() {
        return CreatorStatistics.builder()
            .creatorId(creatorId)
            .subscribeCount(subscribeCount)
            .salesCount(salesCount)
            .reviewCount(reviewCount)
            .totalReviewRate(totalReviewRate)
            .averageRateOfReview(averageRateOfReview)
            .build();
    }
}
