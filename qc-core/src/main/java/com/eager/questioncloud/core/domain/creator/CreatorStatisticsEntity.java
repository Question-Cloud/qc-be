package com.eager.questioncloud.core.domain.creator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "creator_statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatorStatisticsEntity {
    @Id
    private Long creatorId;

    @Column
    private int subscribeCount;

    @Column
    private int salesCount;

    @Column
    private int reviewCount;

    @Column
    private int totalReviewRate;

    @Column
    private Double averageRateOfReview;

    @Builder
    public CreatorStatisticsEntity(Long creatorId, int subscribeCount, int salesCount, int reviewCount, int totalReviewRate,
        Double averageRateOfReview) {
        this.creatorId = creatorId;
        this.subscribeCount = subscribeCount;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.totalReviewRate = totalReviewRate;
        this.averageRateOfReview = averageRateOfReview;
    }

    public static CreatorStatisticsEntity from(CreatorStatistics creatorStatistics) {
        return CreatorStatisticsEntity.builder()
            .creatorId(creatorStatistics.getCreatorId())
            .subscribeCount(creatorStatistics.getSubscribeCount())
            .salesCount(creatorStatistics.getSalesCount())
            .reviewCount(creatorStatistics.getReviewCount())
            .totalReviewRate(creatorStatistics.getTotalReviewRate())
            .averageRateOfReview(creatorStatistics.getAverageRateOfReview())
            .build();
    }

    public CreatorStatistics toModel() {
        return new CreatorStatistics(creatorId, subscribeCount, salesCount, reviewCount, totalReviewRate, averageRateOfReview);
    }
}
