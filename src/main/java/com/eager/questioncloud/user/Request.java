package com.eager.questioncloud.user;

import lombok.Getter;

public class Request {
    @Getter
    public static class CreateUserRequest {
        private String loginId;
        private String password;
        private String socialUid;
        private AccountType accountType;
        private String phone;
        private String name;
        private String email;
    }
}
