package com.eager.questioncloud.application.api.hub.review.dto;

import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionHubReviewControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}
