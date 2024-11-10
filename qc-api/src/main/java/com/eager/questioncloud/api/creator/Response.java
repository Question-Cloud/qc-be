package com.eager.questioncloud.api.creator;

import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @AllArgsConstructor
    @Getter
    public static class CreatorInformationResponse {
        private CreatorInformation creatorInformation;
    }
}
