package com.eager.questioncloud.application.api.workspace.dto;

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel;
import com.eager.questioncloud.core.domain.question.enums.QuestionType;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class WorkSpaceControllerRequest {
    @Getter
    public static class RegisterCreatorRequest {
        @NotNull
        private Subject mainSubject;

        @NotBlank
        private String introduction;
    }

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
            return new QuestionContent(
                questionCategoryId,
                subject,
                title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                price);
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
            return new QuestionContent(
                questionCategoryId,
                subject,
                title,
                description,
                thumbnail,
                fileUrl,
                explanationUrl,
                QuestionType.SelfMade,
                questionLevel,
                price);
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
