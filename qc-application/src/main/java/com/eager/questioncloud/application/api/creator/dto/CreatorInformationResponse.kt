package com.eager.questioncloud.application.api.creator.dto;

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatorResponse {
    @AllArgsConstructor
    @Getter
    public static class CreatorInformationResponse {
        private CreatorInformation creatorInformation;
    }
}
