package com.eager.questioncloud.domain.user.service;

import com.eager.questioncloud.domain.user.implement.CreateSocialUserInformationProcessor;
import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.domain.user.vo.UserAccountInformation;
import com.eager.questioncloud.domain.user.vo.UserInformation;
import com.eager.questioncloud.domain.user.vo.UserStatus;
import com.eager.questioncloud.domain.user.vo.UserType;
import com.eager.questioncloud.domain.user.dto.Request.CreateUserRequest;
import com.eager.questioncloud.domain.user.implement.UserAppender;
import com.eager.questioncloud.domain.user.implement.UserReader;
import com.eager.questioncloud.domain.user.implement.UserUpdater;
import com.eager.questioncloud.domain.verification.implement.EmailVerificationProcessor;
import com.eager.questioncloud.domain.verification.model.EmailVerification;
import com.eager.questioncloud.domain.verification.vo.EmailVerificationType;
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

    public User create(CreateUserRequest createUserRequest) {
        UserAccountInformation userAccountInformation = getUserAccountInformation(createUserRequest);
        UserInformation userInformation = UserInformation.create(createUserRequest);
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

    private UserAccountInformation getUserAccountInformation(CreateUserRequest createUserRequest) {
        if (createUserRequest.getAccountType().equals(AccountType.EMAIL)) {
            return UserAccountInformation.createEmailAccountInformation(createUserRequest.getPassword());
        }
        CreateSocialUserInformation socialUserInformation = createSocialUserInformationProcessor.use(
            createUserRequest.getSocialRegisterToken(),
            createUserRequest.getAccountType());
        return UserAccountInformation.createSocialAccountInformation(socialUserInformation.getSocialUid(), socialUserInformation.getAccountType());
    }
}
