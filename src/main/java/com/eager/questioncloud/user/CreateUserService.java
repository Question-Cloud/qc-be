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
    private final CreateSocialUserInformationCreator createSocialUserInformationCreator;
    private final CreateSocialUserInformationReader createSocialUserInformationReader;
    private final CreateSocialUserInformationUpdater createSocialUserInformationUpdater;
    private final CreateUserEmailVerificationProcessor createUserEmailVerificationProcessor;

    public User create(CreateUser createUser) {
        switch (createUser.getAccountType()) {
            case ID -> {
                User user = userCreator.create(User.create(createUser));
                createUserEmailVerificationProcessor.sendVerificationMail(user);
                return user;
            }
            default -> {
                CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationReader.find(createUser.getSocialRegisterToken());
                createSocialUserInformationUpdater.use(createSocialUserInformation);
                User user = userCreator.create(User.create(createUser, createSocialUserInformation.getSocialUid()));
                createUserEmailVerificationProcessor.sendVerificationMail(user);
                return user;
            }
        }
    }

    public CreateSocialUserInformation createSocialUserInformation(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationCreator.create(createSocialUserInformation);
    }

    public void verifyCreateUser(String token, EmailVerificationType emailVerificationType) {
        EmailVerification emailVerification = createUserEmailVerificationProcessor.verify(token, emailVerificationType);
        User user = userReader.getUser(emailVerification.getUid());
        userUpdater.verifyUser(user);
    }
}
