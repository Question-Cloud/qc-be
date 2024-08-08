package com.eager.questioncloud.user;

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

    public User create(CreateUser createUser) {
        if (createUser.getAccountType().equals(AccountType.ID)) {
            return userCreator.create(User.create(createUser));
        }

        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationReader.find(createUser.getSocialRegisterToken());
        createSocialUserInformationUpdater.use(createSocialUserInformation);

        return userCreator.create(User.create(createUser, createSocialUserInformation.getSocialUid()));
    }

    public CreateSocialUserInformation createSocialUserInformation(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationCreator.create(createSocialUserInformation);
    }

    public void verifyCreateUser(Long uid) {
        User user = userReader.getUser(uid);
        userUpdater.verifyUser(user);
    }
}
