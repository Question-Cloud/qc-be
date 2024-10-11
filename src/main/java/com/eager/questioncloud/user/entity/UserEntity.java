package com.eager.questioncloud.user.entity;

import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.vo.AccountType;
import com.eager.questioncloud.user.vo.UserStatus;
import com.eager.questioncloud.user.vo.UserType;
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
    private String email;

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

    @Column
    private String profileImage;

    @Column
    private int point;

    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    public UserEntity(Long uid, String email, String password, String socialUid, AccountType accountType, String phone, String name,
        String profileImage, int point, UserType userType, UserStatus userStatus) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.socialUid = socialUid;
        this.accountType = accountType;
        this.phone = phone;
        this.name = name;
        this.profileImage = profileImage;
        this.point = point;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public User toDomain() {
        return User.builder()
            .uid(uid)
            .email(email)
            .password(password)
            .socialUid(socialUid)
            .accountType(accountType)
            .phone(phone)
            .name(name)
            .profileImage(profileImage)
            .point(point)
            .userType(userType)
            .userStatus(userStatus)
            .build();
    }
}
