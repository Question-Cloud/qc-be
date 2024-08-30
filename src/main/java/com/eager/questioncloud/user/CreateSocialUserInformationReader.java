package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationReader {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation find(String registerToken, AccountType accountType) {
        return createSocialUserInformationRepository.find(registerToken, accountType);
    }
}
