package com.eager.questioncloud.review.entity;

import com.eager.questioncloud.review.domain.QuestionReviewStatistics;
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
@Table(name = "question_review_statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionReviewStatisticsEntity {
    @Id
    private Long questionId;

    @Column
    private int reviewCount;

    @Column
    private int totalRate;

    @Column
    private double averageRate;

    @Builder
    public QuestionReviewStatisticsEntity(Long questionId, int reviewCount, int totalRate, double averageRate) {
        this.questionId = questionId;
        this.reviewCount = reviewCount;
        this.totalRate = totalRate;
        this.averageRate = averageRate;
    }

    public QuestionReviewStatistics toModel() {
        return QuestionReviewStatistics.builder()
            .questionId(questionId)
            .reviewCount(reviewCount)
            .totalRate(totalRate)
            .averageRate(averageRate)
            .build();
    }
}