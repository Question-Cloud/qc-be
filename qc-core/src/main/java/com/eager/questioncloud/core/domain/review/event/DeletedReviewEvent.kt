package com.eager.questioncloud.core.domain.review.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeletedReviewEvent {
    private Long questionId;
    private int rate;

    public static DeletedReviewEvent create(Long questionId, int rate) {
        return new DeletedReviewEvent(questionId, rate);
    }
}
