package com.eager.questioncloud.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterQuestionReviewRequest {
        @NotNull
        private Long questionId;

        @Min(value = 1)
        @Max(value = 5)
        private int rate;

        @NotBlank
        private String comment;
    }

    @Getter
    public static class ModifyQuestionReviewRequest {
        @Min(value = 1)
        @Max(value = 5)
        private int rate;

        @NotBlank
        private String comment;
    }
}
