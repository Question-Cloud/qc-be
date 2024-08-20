package com.eager.questioncloud.user;

import com.eager.questioncloud.mail.CreateUserEmailVerificationProcessor;
import com.eager.questioncloud.mail.EmailVerification;
import com.eager.questioncloud.mail.EmailVerificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserService {
    private final UserCreator userCreator;
    private final UserUpdater userUpdater;
    private final UserReader userReader;
    private final CreateSocialUserInformationProcessor createSocialUserInformationProcessor;
    private final CreateUserEmailVerificationProcessor createUserEmailVerificationProcessor;

    public User create(CreateUser createUser) {
        if (createUser.getAccountType().equals(AccountType.EMAIL)) {
            return userCreator.create(User.create(createUser));
        }
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationProcessor.use(
            createUser.getSocialRegisterToken(),
            createUser.getAccountType());
        return userCreator.create(User.create(createUser, createSocialUserInformation.getSocialUid()));
    }

    public EmailVerification sendCreateUserVerifyMail(User user) {
        return createUserEmailVerificationProcessor.sendVerificationMail(user);
    }

    public void resend(String resendToken) {
        createUserEmailVerificationProcessor.resendVerificationMail(resendToken);
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = createUserEmailVerificationProcessor.verify(token, emailVerificationType);
        User user = userReader.getUser(emailVerification.getUid());
        userUpdater.verifyUser(user);
    }
}
