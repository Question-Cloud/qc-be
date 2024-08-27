package com.eager.questioncloud.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Response {
    @Getter
    @AllArgsConstructor
    public static class GetUserPointResponse {
        private int point;
    }
}
