package com.eager.questioncloud.application.user.point;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserPointControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class GetUserPointResponse {
        private int point;
    }
}
