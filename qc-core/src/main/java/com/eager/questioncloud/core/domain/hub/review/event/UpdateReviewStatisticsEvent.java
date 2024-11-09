package com.eager.questioncloud.core.domain.hub.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateReviewStatisticsEvent {
    private Long questionId;
    private int varianceRate;

    public static UpdateReviewStatisticsEvent create(Long questionId, int varianceRate) {
        return new UpdateReviewStatisticsEvent(questionId, varianceRate);
    }
}
