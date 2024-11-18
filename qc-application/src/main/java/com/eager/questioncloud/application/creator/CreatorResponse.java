package com.eager.questioncloud.application.creator;

import com.eager.questioncloud.domain.creator.CreatorInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CreatorResponse {
    @AllArgsConstructor
    @Getter
    public static class CreatorInformationResponse {
        private CreatorInformation creatorInformation;
    }
}
