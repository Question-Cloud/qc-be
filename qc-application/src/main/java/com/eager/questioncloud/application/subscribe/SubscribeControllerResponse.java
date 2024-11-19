package com.eager.questioncloud.application.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SubscribeControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class CreatorSubscribeInformationResponse {
        private Boolean isSubscribed;
        private int count;
    }
}
