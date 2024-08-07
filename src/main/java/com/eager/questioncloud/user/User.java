package com.eager.questioncloud.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long uid;
    private String loginId;
    private String password;
    private String socialUid;
    private AccountType accountType;
    private String phone;
    private String name;
    private String email;

    @Builder
    public User(Long uid, String loginId, String password, String socialUid, AccountType accountType, String phone, String name, String email) {
        this.uid = uid;
        this.loginId = loginId;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
        this.email = email;
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
            .uid(uid)
            .loginId(loginId)
            .password(password)
            .socialUid(socialUid)
            .accountType(accountType)
            .phone(phone)
            .name(name)
            .email(email)
            .build();
    }
}
