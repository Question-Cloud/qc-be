package com.eager.questioncloud.application.api.user.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class UserPointControllerRequest {
    @Getter
    public static class UpdateMyInformationRequest {
        private String profileImage;
        @NotBlank
        private String name;
    }
}
