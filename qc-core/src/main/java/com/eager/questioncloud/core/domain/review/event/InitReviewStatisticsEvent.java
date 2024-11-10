package com.eager.questioncloud.core.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InitReviewStatisticsEvent {
    private Long questionId;

    public static InitReviewStatisticsEvent create(Long questionId) {
        return new InitReviewStatisticsEvent(questionId);
    }
}
