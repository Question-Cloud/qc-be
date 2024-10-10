package com.eager.questioncloud.user.dto;

import com.eager.questioncloud.user.dto.UserDto.MyInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Response {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class CreateUserResponse {
        private String resendToken;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class MyInformationResponse {
        private MyInformation myInformation;
    }
}
