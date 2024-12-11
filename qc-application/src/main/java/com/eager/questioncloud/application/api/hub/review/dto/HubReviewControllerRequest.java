package com.eager.questioncloud.application.api.hub.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class HubReviewControllerRequest {
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
