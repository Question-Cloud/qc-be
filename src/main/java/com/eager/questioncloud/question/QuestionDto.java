package com.eager.questioncloud.question;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionDto {
    @Getter
    @AllArgsConstructor
    public static class QuestionFilterItem {
        private Long id;
        private String title;
        private String parentCategory;
        private String childCategory;
        private String thumbnail;
        private String creatorName;
        private QuestionLevel questionLevel;
        private int price;
        private Double rate;
        private Boolean isOwned;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionDetail {
        private Long id;
        private String title;
        private String creator;
        private Subject subject;
        private String parentCategory;
        private String childCategory;
        private QuestionLevel questionLevel;
        private LocalDateTime createdAt;
        private int price;
        private Boolean isOwned;
    }
}
