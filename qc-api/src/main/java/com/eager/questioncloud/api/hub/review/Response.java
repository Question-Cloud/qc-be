package com.eager.questioncloud.api.hub.review;

import com.eager.questioncloud.core.domain.hub.review.dto.QuestionReviewDto.MyQuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}
