package com.eager.questioncloud.application.api.creator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class RegisterCreatorControllerResponse {
    @AllArgsConstructor
    @Getter
    public static class RegisterCreatorResponse {
        private Long creatorId;
    }
}
