package com.eager.questioncloud.domain.creator.dto;

import com.eager.questioncloud.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.domain.creator.vo.CreatorProfile;
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
