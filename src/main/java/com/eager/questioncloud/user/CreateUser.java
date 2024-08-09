package com.eager.questioncloud.user;

import com.eager.questioncloud.user.Request.CreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUser {
    private String loginId;
    private String password;
    private String socialRegisterToken;
    private AccountType accountType;
    private String phone;
    private String name;
    private String email;

    public static CreateUser toDomain(CreateUserRequest createUserRequest) {
        return new CreateUser(
            createUserRequest.getLoginId(),
            createUserRequest.getPassword(),
            createUserRequest.getSocialRegisterToken(),
            createUserRequest.getAccountType(),
            createUserRequest.getPhone(),
            createUserRequest.getName(),
            createUserRequest.getEmail());
    }
}
