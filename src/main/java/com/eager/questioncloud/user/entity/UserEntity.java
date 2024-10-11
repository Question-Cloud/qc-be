package com.eager.questioncloud.user.entity;

import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.vo.UserAccountInformation;
import com.eager.questioncloud.user.vo.UserInformation;
import com.eager.questioncloud.user.vo.UserStatus;
import com.eager.questioncloud.user.vo.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

    @Embedded
    private UserAccountInformation userAccountInformation;

    @Embedded
    private UserInformation userInformation;

    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column
    private int point;

    @Builder
    public UserEntity(Long uid, UserAccountInformation userAccountInformation, UserInformation userInformation, UserType userType,
        UserStatus userStatus, int point) {
        this.uid = uid;
        this.userAccountInformation = userAccountInformation;
        this.userInformation = userInformation;
        this.userType = userType;
        this.userStatus = userStatus;
        this.point = point;
    }

    public User toModel() {
        return User.builder()
            .uid(uid)
            .userAccountInformation(userAccountInformation)
            .userInformation(userInformation)
            .userType(userType)
            .userStatus(userStatus)
            .point(point)
            .build();
    }
}
