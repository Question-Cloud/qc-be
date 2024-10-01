package com.eager.questioncloud.question;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterSelfMadeQuestionRequest {
        @NotNull
        private Long questionCategoryId;

        @NotNull
        private Subject subject;

        @NotBlank
        private String title;

        @NotBlank
        private String description;

        @NotBlank
        private String thumbnail;

        @NotBlank
        private String fileUrl;

        @NotBlank
        private String explanationUrl;

        @NotNull
        private QuestionLevel questionLevel;

        @Min(value = 100)
        private int price;

        public QuestionContent toModel() {
            return new QuestionContent(
                questionCategoryId,
                subject, title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                QuestionStatus.Available,
                price);
        }
    }

    @Getter
    public static class ModifySelfMadeQuestionRequest {
        @NotNull
        private Long questionCategoryId;

        @NotNull
        private Subject subject;

        @NotBlank
        private String title;

        @NotBlank
        private String description;

        @NotBlank
        private String thumbnail;

        @NotBlank
        private String fileUrl;

        @NotBlank
        private String explanationUrl;

        @NotNull
        private QuestionLevel questionLevel;

        @NotNull
        private QuestionStatus questionStatus;

        @Min(value = 100)
        private int price;

        public QuestionContent toModel() {
            return new QuestionContent(
                questionCategoryId,
                subject, title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                questionStatus,
                price);
        }
    }
}
