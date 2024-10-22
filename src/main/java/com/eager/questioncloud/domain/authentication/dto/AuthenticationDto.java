package com.eager.questioncloud.domain.authentication.dto;

import com.eager.questioncloud.domain.creator.model.Creator;
import com.eager.questioncloud.domain.user.model.User;
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
