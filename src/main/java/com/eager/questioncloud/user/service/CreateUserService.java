package com.eager.questioncloud.user.service;

import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.mail.implement.EmailVerificationProcessor;
import com.eager.questioncloud.user.dto.Request.CreateUserRequest;
import com.eager.questioncloud.user.implement.CreateSocialUserInformationProcessor;
import com.eager.questioncloud.user.implement.UserAppender;
import com.eager.questioncloud.user.implement.UserReader;
import com.eager.questioncloud.user.implement.UserUpdater;
import com.eager.questioncloud.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.vo.AccountType;
import com.eager.questioncloud.user.vo.UserAccountInformation;
import com.eager.questioncloud.user.vo.UserInformation;
import com.eager.questioncloud.user.vo.UserStatus;
import com.eager.questioncloud.user.vo.UserType;
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
        UserInformation userInformation = UserInformation.from(createUserRequest);
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
            return new UserAccountInformation(createUserRequest.getPassword(), null, AccountType.EMAIL);
        }
        CreateSocialUserInformation socialUserInformation = createSocialUserInformationProcessor.use(
            createUserRequest.getSocialRegisterToken(),
            createUserRequest.getAccountType());
        return new UserAccountInformation(
            null,
            socialUserInformation.getSocialUid(),
            socialUserInformation.getAccountType());
    }
}
