package com.eager.questioncloud.application.api.user.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserPointControllerResponse {
    @Getter
    @AllArgsConstructor
    public static class GetUserPointResponse {
        private int point;
    }
}
