package com.eager.questioncloud.core.domain.user;

import com.eager.questioncloud.core.domain.user.enums.UserStatus;
import com.eager.questioncloud.core.domain.user.enums.UserType;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.model.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.model.UserInformation;
import lombok.Builder;

@Builder
public class UserBuilder {
    private Long uid;
    @Builder.Default
    private UserAccountInformation userAccountInformation = UserAccountInformation.guest;
    @Builder.Default
    private UserInformation userInformation = UserInformation.guest;
    @Builder.Default
    private UserType userType = UserType.NormalUser;
    @Builder.Default
    private UserStatus userStatus = UserStatus.Active;

    public User toUser() {
        return User.builder()
            .uid(uid)
            .userAccountInformation(userAccountInformation)
            .userInformation(userInformation)
            .userType(userType)
            .userStatus(userStatus)
            .build();
    }
}
