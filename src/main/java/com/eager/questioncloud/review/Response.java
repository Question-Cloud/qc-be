package com.eager.questioncloud.review;

import com.eager.questioncloud.review.QuestionReviewDto.MyQuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}