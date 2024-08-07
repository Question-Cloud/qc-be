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

        public User toDomain() {
            return User.builder()
                .loginId(loginId)
                .password(accountType.equals(AccountType.ID) ? null : PasswordProcessor.encode(password))
                .socialUid(socialUid)
                .accountType(accountType)
                .phone(phone)
                .name(name)
                .build();
        }
    }
}
