package com.eager.questioncloud.creator.dto;

import com.eager.questioncloud.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.dto.CreatorDto.MyCreatorInformation;
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
//        private Boolean isSubscribe;
    }

    @Getter
    @AllArgsConstructor
    public static class MyCreatorInformationResponse {
        private MyCreatorInformation information;
    }
}
