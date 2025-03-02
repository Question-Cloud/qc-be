package com.eager.questioncloud.core.domain.user.infrastructure.entity;

import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.model.User;
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
    private UserAccountInformationEntity userAccountInformationEntity;

    @Embedded
    private UserInformationEntity userInformationEntity;

    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    public UserEntity(Long uid, UserAccountInformationEntity userAccountInformationEntity, UserInformationEntity userInformationEntity,
        UserType userType,
        UserStatus userStatus) {
        this.uid = uid;
        this.userAccountInformationEntity = userAccountInformationEntity;
        this.userInformationEntity = userInformationEntity;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public User toModel() {
        return User.builder()
            .uid(uid)
            .userAccountInformation(userAccountInformationEntity.toModel())
            .userInformation(userInformationEntity.toModel())
            .userType(userType)
            .userStatus(userStatus)
            .build();
    }

    public static UserEntity from(User user) {
        return UserEntity.builder()
            .uid(user.getUid())
            .userAccountInformationEntity(UserAccountInformationEntity.from(user.getUserAccountInformation()))
            .userInformationEntity(UserInformationEntity.from(user.getUserInformation()))
            .userType(user.getUserType())
            .userStatus(user.getUserStatus())
            .build();
    }
}
