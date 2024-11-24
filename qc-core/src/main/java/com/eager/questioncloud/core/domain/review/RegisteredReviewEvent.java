package com.eager.questioncloud.core.domain.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisteredReviewEvent {
    private Long questionId;
    private int rate;

    public static RegisteredReviewEvent create(Long questionId, int rate) {
        return new RegisteredReviewEvent(questionId, rate);
    }
}
