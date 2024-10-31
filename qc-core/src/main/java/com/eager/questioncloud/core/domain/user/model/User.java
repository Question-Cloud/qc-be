package com.eager.questioncloud.core.domain.user.model;

import com.eager.questioncloud.core.domain.user.vo.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.vo.UserInformation;
import com.eager.questioncloud.core.domain.user.vo.UserStatus;
import com.eager.questioncloud.core.domain.user.vo.UserType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long uid;
    private UserAccountInformation userAccountInformation;
    private UserInformation userInformation;
    private UserType userType;
    private UserStatus userStatus;

    @Builder
    public User(Long uid, UserAccountInformation userAccountInformation, UserInformation userInformation, UserType userType, UserStatus userStatus) {
        this.uid = uid;
        this.userAccountInformation = userAccountInformation;
        this.userInformation = userInformation;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public static User create(UserAccountInformation userAccountInformation, UserInformation userInformation, UserType userType,
        UserStatus userStatus) {
        return User.builder()
            .userAccountInformation(userAccountInformation)
            .userInformation(userInformation)
            .userType(userType)
            .userStatus(userStatus)
            .build();
    }

    public static User guest() {
        return User.builder()
            .uid(-1L)
            .userInformation(UserInformation.getGuestInformation())
            .userAccountInformation(UserAccountInformation.getGuestAccountInformation())
            .build();
    }

    public void active() {
        this.userStatus = UserStatus.Active;
    }

    //TODO Exception 처리
    public void checkUserStatus() {
        if (userStatus.equals(UserStatus.PendingEmailVerification)) {
            throw new CustomException(Error.NOT_ACTIVE_USER);
//            throw new NotVerificationUserException(this);
        }
        if (!userStatus.equals(UserStatus.Active)) {
            throw new CustomException(Error.NOT_ACTIVE_USER);
        }
    }

    public void changePassword(String newPassword) {
        userAccountInformation = userAccountInformation.changePassword(newPassword);
    }

    public User update(String name, String profileImage) {
        userInformation = userInformation.updateUserInformation(name, profileImage);
        return this;
    }
}
