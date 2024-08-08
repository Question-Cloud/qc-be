package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserService {
    private final UserCreator userCreator;
    private final CreateSocialUserInformationCreator createSocialUserInformationCreator;

    public User create(User user) {
        return userCreator.create(user);
    }

    public CreateSocialUserInformation createSocialUserInformation(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationCreator.create(createSocialUserInformation);
    }
}
