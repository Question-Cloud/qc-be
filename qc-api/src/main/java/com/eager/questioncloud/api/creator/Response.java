package com.eager.questioncloud.api.creator;

import com.eager.questioncloud.core.domain.questionhub.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.questionhub.creator.vo.CreatorProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @AllArgsConstructor
    @Getter
    public static class RegisterCreatorResponse {
        private Long creatorId;
    }

    @AllArgsConstructor
    @Getter
    public static class CreatorInformationResponse {
        private CreatorInformation creatorInformation;
    }

    @Getter
    @AllArgsConstructor
    public static class CreatorProfileResponse {
        private CreatorProfile profile;
    }
}
