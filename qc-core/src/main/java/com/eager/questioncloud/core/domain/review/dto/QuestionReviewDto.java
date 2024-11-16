package com.eager.questioncloud.core.domain.review.dto;

import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionReviewDto {
    @Getter
    @AllArgsConstructor
    public static class MyQuestionReview {
        private Long id;
        private Integer rate;
        private String comment;

        public static MyQuestionReview from(QuestionReview questionReview) {
            return new MyQuestionReview(questionReview.getId(), questionReview.getRate(), questionReview.getComment());
        }
    }
}
