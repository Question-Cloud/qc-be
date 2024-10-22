package com.eager.questioncloud.authentication.dto;

import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthenticationDto {
    @Getter
    @AllArgsConstructor
    public static class UserWithCreator {
        private User user;
        private Creator creator;
    }
}
