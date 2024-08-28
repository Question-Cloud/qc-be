package com.eager.questioncloud.subscribe;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class IsSubscribedResponse {
        private Boolean isSubscribed;
    }
}
