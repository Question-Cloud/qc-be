package com.eager.questioncloud.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String loginId;

    @Column
    private String password;

    @Column
    private String socialUid;

    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column
    private String phone;

    @Column
    private String name;

    @Builder
    public UserEntity(Long uid, String loginId, String password, String socialUid, AccountType accountType, String phone,
        String name) {
        this.uid = uid;
        this.loginId = loginId;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
    }

    public User toDomain() {
        return User.builder()
            .uid(uid)
            .loginId(loginId)
            .password(password)
            .socialUid(socialUid)
            .accountType(accountType)
            .phone(phone)
            .name(name)
            .build();
    }
}
