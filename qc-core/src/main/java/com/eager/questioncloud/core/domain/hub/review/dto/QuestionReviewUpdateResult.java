package com.eager.questioncloud.core.domain.hub.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionReviewUpdateResult {
    private Long questionId;
    private int varianceRate;
}
