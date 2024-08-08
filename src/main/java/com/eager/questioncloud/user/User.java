package com.eager.questioncloud.user;

import com.eager.questioncloud.user.Request.CreateUserRequest;
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

    public static User create(CreateUserRequest createUserRequest) {
        if (createUserRequest.getAccountType().equals(AccountType.ID)) {
            return User.builder()
                .loginId(createUserRequest.getLoginId())
                .password(PasswordProcessor.encode(createUserRequest.getPassword()))
                .accountType(createUserRequest.getAccountType())
                .phone(createUserRequest.getPhone())
                .name(createUserRequest.getName())
                .email(createUserRequest.getEmail())
                .userStatus(UserStatus.PendingEmailVerification)
                .build();
        }
        return User.builder()
            .socialUid(createUserRequest.getSocialUid())
            .accountType(createUserRequest.getAccountType())
            .phone(createUserRequest.getPhone())
            .name(createUserRequest.getName())
            .email(createUserRequest.getEmail())
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
