package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationProcessor {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation create(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationRepository.append(createSocialUserInformation);
    }

    public CreateSocialUserInformation use(String token, AccountType accountType) {
        CreateSocialUserInformation createSocialUserInformation = createSocialUserInformationRepository.find(token, accountType);
        createSocialUserInformation.use();
        return createSocialUserInformationRepository.save(createSocialUserInformation);
    }
}
