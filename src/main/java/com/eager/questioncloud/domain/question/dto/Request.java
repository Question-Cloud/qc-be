package com.eager.questioncloud.domain.question.dto;

import com.eager.questioncloud.domain.question.vo.QuestionLevel;
import com.eager.questioncloud.domain.question.vo.Subject;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
    @Getter
    public static class SelfMadeQuestionRequest {
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
    }
}
