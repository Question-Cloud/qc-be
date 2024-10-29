package com.eager.questioncloud.core.domain.user.service;

import com.eager.questioncloud.core.domain.user.dto.CreateUser;
import com.eager.questioncloud.core.domain.user.implement.CreateSocialUserInformationProcessor;
import com.eager.questioncloud.core.domain.user.implement.UserAppender;
import com.eager.questioncloud.core.domain.user.implement.UserReader;
import com.eager.questioncloud.core.domain.user.implement.UserUpdater;
import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.model.User;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.domain.user.vo.UserAccountInformation;
import com.eager.questioncloud.core.domain.user.vo.UserInformation;
import com.eager.questioncloud.core.domain.user.vo.UserStatus;
import com.eager.questioncloud.core.domain.user.vo.UserType;
import com.eager.questioncloud.core.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.core.domain.verification.model.EmailVerification;
import com.eager.questioncloud.core.domain.verification.vo.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserService {
    private final UserAppender userAppender;
    private final UserUpdater userUpdater;
    private final UserReader userReader;
    private final CreateSocialUserInformationProcessor createSocialUserInformationProcessor;
    private final EmailVerificationProcessor emailVerificationProcessor;

    public User create(CreateUser createUser) {
        UserAccountInformation userAccountInformation = getUserAccountInformation(createUser);
        UserInformation userInformation = UserInformation.create(createUser);
        return userAppender.create(User.create(userAccountInformation, userInformation, UserType.NormalUser, UserStatus.PendingEmailVerification));
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        return emailVerificationProcessor.sendVerificationMail(user, EmailVerificationType.CreateUser);
    }

    public void resend(String resendToken) {
        emailVerificationProcessor.resendVerificationMail(resendToken);
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = emailVerificationProcessor.verify(token, emailVerificationType);
        User user = userReader.getUser(emailVerification.getUid());
        userUpdater.verifyUser(user);
    }

    private UserAccountInformation getUserAccountInformation(CreateUser createUser) {
        if (createUser.accountType().equals(AccountType.EMAIL)) {
            return UserAccountInformation.createEmailAccountInformation(createUser.password());
        }
        CreateSocialUserInformation socialUserInformation = createSocialUserInformationProcessor.use(
            createUser.socialRegisterToken(),
            createUser.accountType());
        return UserAccountInformation.createSocialAccountInformation(socialUserInformation.getSocialUid(), socialUserInformation.getAccountType());
    }
}
