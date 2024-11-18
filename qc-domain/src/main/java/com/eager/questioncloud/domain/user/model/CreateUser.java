package com.eager.questioncloud.domain.user.model;

import com.eager.questioncloud.domain.user.vo.AccountType;
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
