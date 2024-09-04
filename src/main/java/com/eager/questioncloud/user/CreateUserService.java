package com.eager.questioncloud.user;

import com.eager.questioncloud.mail.EmailVerification;
import com.eager.questioncloud.mail.EmailVerificationProcessor;
import com.eager.questioncloud.mail.EmailVerificationType;
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
