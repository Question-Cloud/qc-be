package com.eager.questioncloud.question;

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
}
