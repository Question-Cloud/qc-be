package com.eager.questioncloud.application.creator;

import com.eager.questioncloud.domain.question.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class RegisterCreatorControllerRequest {
    @Getter
    public static class RegisterCreatorRequest {
        @NotNull
        private Subject mainSubject;

        @NotBlank
        private String introduction;
    }
}
