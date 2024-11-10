package com.eager.questioncloud.core.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReviewStatisticsEvent {
    private Long questionId;
    private int varianceRate;
    private UpdateReviewType updateReviewType;

    public static UpdateReviewStatisticsEvent create(Long questionId, int varianceRate, UpdateReviewType updateReviewType) {
        return new UpdateReviewStatisticsEvent(questionId, varianceRate, updateReviewType);
    }
}
