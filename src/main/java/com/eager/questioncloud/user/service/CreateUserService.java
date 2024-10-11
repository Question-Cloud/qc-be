package com.eager.questioncloud.user.service;

import com.eager.questioncloud.mail.domain.EmailVerification;
import com.eager.questioncloud.mail.domain.EmailVerificationType;
import com.eager.questioncloud.mail.implement.EmailVerificationProcessor;
import com.eager.questioncloud.user.implement.CreateSocialUserInformationProcessor;
import com.eager.questioncloud.user.implement.UserAppender;
import com.eager.questioncloud.user.implement.UserReader;
import com.eager.questioncloud.user.implement.UserUpdater;
import com.eager.questioncloud.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.user.model.CreateUser;
import com.eager.questioncloud.user.model.User;
import com.eager.questioncloud.user.vo.AccountType;
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
        if (createUser.getAccountType().equals(AccountType.EMAIL)) {
            return userAppender.create(User.create(createUser));
        }
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationProcessor.use(
            createUser.getSocialRegisterToken(),
            createUser.getAccountType());
        return userAppender.create(User.create(createUser, createSocialUserInformation.getSocialUid()));
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
}
