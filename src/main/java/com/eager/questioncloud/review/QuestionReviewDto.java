package com.eager.questioncloud.review;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionReviewDto {
    @Getter
    @AllArgsConstructor
    public static class QuestionReviewItem {
        private Long id;
        private String name;
        private Boolean isCreator;
        private Boolean isWriter;
        private Integer reviewCount;
        private Double rateAverage;
        private Integer rate;
        private String comment;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class MyQuestionReview {
        private Long id;
        private Integer rate;
        private String comment;

        public static MyQuestionReview of(QuestionReview questionReview) {
            return new MyQuestionReview(questionReview.getId(), questionReview.getRate(), questionReview.getComment());
        }
    }
}
