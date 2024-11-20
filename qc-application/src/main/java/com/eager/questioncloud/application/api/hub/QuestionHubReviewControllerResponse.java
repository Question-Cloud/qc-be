package com.eager.questioncloud.application.api.hub;

import com.eager.questioncloud.domain.review.MyQuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionHubReviewControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}
