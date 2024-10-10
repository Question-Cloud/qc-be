package com.eager.questioncloud.user.implement;

import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.CreateSocialUserInformation;
import com.eager.questioncloud.user.repository.CreateSocialUserInformationRepository;
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
