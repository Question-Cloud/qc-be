package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.core.domain.creator.CreatorInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatorResponse {
    @AllArgsConstructor
    @Getter
    public static class CreatorInformationResponse {
        private CreatorInformation creatorInformation;
    }
}
