package com.eager.questioncloud.api.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class CreatorSubscribeInformationResponse {
        private Boolean isSubscribed;
        private int count;
    }
}
