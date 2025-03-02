package com.eager.questioncloud.core.domain.user.model;

import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.implement.PasswordProcessor;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.NotVerificationUserException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    public void checkUserStatus() {
        if (userStatus.equals(UserStatus.PendingEmailVerification)) {
            throw new NotVerificationUserException(uid);
        }
        if (!userStatus.equals(UserStatus.Active)) {
            throw new CoreException(Error.NOT_ACTIVE_USER);
        }
    }

    public void changePassword(String newPassword) {
        userAccountInformation = userAccountInformation.changePassword(newPassword);
    }

    public void passwordAuthentication(String rawPassword) {
        if (!PasswordProcessor.matches(rawPassword, userAccountInformation.getPassword())) {
            throw new CoreException(Error.FAIL_LOGIN);
        }
    }

    public void updateUserInformation(String name, String profileImage) {
        userInformation = userInformation.updateUserInformation(name, profileImage);
    }

    public void setCreator() {
        this.userType = UserType.CreatorUser;
    }
}
