package com.eager.questioncloud.api.creator;

import com.eager.questioncloud.core.domain.hub.question.vo.Subject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class Request {
    @Getter
    public static class RegisterCreatorRequest {
        @NotNull
        private Subject mainSubject;

        @NotBlank
        private String introduction;
    }

    @Getter
    public static class UpdateCreatorProfileRequest {
        @NotNull
        private Subject mainSubject;

        @NotBlank
        private String introduction;
    }
}
