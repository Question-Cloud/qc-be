package com.eager.questioncloud.api.user.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class Request {
    @Getter
    public static class UpdateMyInformationRequest {
        private String profileImage;
        @NotBlank
        private String name;
    }
}
