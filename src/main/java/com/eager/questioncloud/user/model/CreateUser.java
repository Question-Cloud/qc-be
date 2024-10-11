package com.eager.questioncloud.user.model;

import com.eager.questioncloud.user.dto.Request.CreateUserRequest;
import com.eager.questioncloud.user.vo.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateUser {
    private String email;
    private String password;
    private String socialRegisterToken;
    private AccountType accountType;
    private String phone;
    private String name;


    public static CreateUser toDomain(CreateUserRequest createUserRequest) {
        return new CreateUser(
            createUserRequest.getEmail(),
            createUserRequest.getPassword(),
            createUserRequest.getSocialRegisterToken(),
            createUserRequest.getAccountType(),
            createUserRequest.getPhone(),
            createUserRequest.getName());
    }
}
