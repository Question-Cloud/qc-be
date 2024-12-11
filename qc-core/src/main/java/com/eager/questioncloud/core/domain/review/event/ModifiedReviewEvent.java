package com.eager.questioncloud.core.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifiedReviewEvent {
    private Long questionId;
    private int varianceRate;

    public static ModifiedReviewEvent create(Long questionId, int varianceRate) {
        return new ModifiedReviewEvent(questionId, varianceRate);
    }
}
