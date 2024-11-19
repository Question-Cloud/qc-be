package com.eager.questioncloud.application.workspace;

import com.eager.questioncloud.domain.question.QuestionContent;
import com.eager.questioncloud.domain.question.QuestionLevel;
import com.eager.questioncloud.domain.question.QuestionType;
import com.eager.questioncloud.domain.question.Subject;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class WorkSpaceControllerRequest {
    @Getter
    public static class RegisterQuestionRequest {
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

        public QuestionContent toQuestionContent() {
            return QuestionContent.builder()
                .questionCategoryId(questionCategoryId)
                .subject(subject)
                .title(title)
                .description(description)
                .thumbnail(thumbnail)
                .fileUrl(fileUrl)
                .explanationUrl(explanationUrl)
                .questionType(QuestionType.SelfMade)
                .questionLevel(questionLevel)
                .price(price)
                .build();
        }
    }

    @Getter
    public static class ModifyQuestionRequest {
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

        public QuestionContent toQuestionContent() {
            return QuestionContent.builder()
                .questionCategoryId(questionCategoryId)
                .subject(subject)
                .title(title)
                .description(description)
                .thumbnail(thumbnail)
                .fileUrl(fileUrl)
                .explanationUrl(explanationUrl)
                .questionType(QuestionType.SelfMade)
                .questionLevel(questionLevel)
                .price(price)
                .build();
        }
    }

    @Getter
    public static class UpdateCreatorProfileRequest {
        @NotNull
        private Subject mainSubject;

        @NotBlank
        private String introduction;
    }
}
