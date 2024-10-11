package com.eager.questioncloud.question.dto;

import com.eager.questioncloud.question.domain.QuestionLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class QuestionDto {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class QuestionInformation {
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
    public static class QuestionInformationForLibrary {
        private Long id;
        private String title;
        private String parentCategory;
        private String childCategory;
        private String thumbnail;
        private String creatorName;
        private QuestionLevel questionLevel;
        private String fileUrl;
        private String explanationUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionInformationForWorkSpace {
        private Long id;
        private String title;
        private String parentCategory;
        private String childCategory;
        private String thumbnail;
        private QuestionLevel questionLevel;
        private int price;
    }
}