package com.eager.questioncloud.domain.review.dto;

import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.MyQuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReviewResponse {
        private MyQuestionReview review;
    }
}
