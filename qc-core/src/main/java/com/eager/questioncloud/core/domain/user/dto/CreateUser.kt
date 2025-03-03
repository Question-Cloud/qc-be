package com.eager.questioncloud.core.domain.user.dto;

import com.eager.questioncloud.core.domain.user.enums.AccountType;
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
}
