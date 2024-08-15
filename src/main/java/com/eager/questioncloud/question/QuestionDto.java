package com.eager.questioncloud.question;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class QuestionDto {
    @Getter
    @AllArgsConstructor
    public static class QuestionFilterItem {
        private Long id;
        private String title;
        private String thumbnail;
        private String creatorName;
        private QuestionLevel questionLevel;
        private int price;
    }
}
