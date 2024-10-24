package com.eager.questioncloud.core.domain.user.dto;

import com.eager.questioncloud.core.domain.user.vo.AccountType;

public record CreateUser(String email, String password, String socialRegisterToken, AccountType accountType, String phone, String name) {
}
