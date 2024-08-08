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
    private UserStatus userStatus;

    @Builder
    public User(Long uid, String loginId, String password, String socialUid, AccountType accountType, String phone, String name, String email,
        UserStatus userStatus) {
        this.uid = uid;
        this.loginId = loginId;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.userStatus = userStatus;
    }

    public void checkUserStatus() {
        if (userStatus.equals(UserStatus.PendingEmailVerification)) {
            throw new RuntimeException();
        }
        if (!userStatus.equals(UserStatus.Active)) {
            throw new RuntimeException();
        }
    }

    public static User create(CreateUser createUser) {
        return User.builder()
            .loginId(createUser.getLoginId())
            .password(PasswordProcessor.encode(createUser.getPassword()))
            .accountType(createUser.getAccountType())
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .email(createUser.getEmail())
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
    }

    public static User create(CreateUser createUser, String socialUid) {
        return User.builder()
            .socialUid(socialUid)
            .accountType(createUser.getAccountType())
            .phone(createUser.getPhone())
            .name(createUser.getName())
            .email(createUser.getEmail())
            .userStatus(UserStatus.PendingEmailVerification)
            .build();
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
            .userStatus(userStatus)
            .build();
    }
}
