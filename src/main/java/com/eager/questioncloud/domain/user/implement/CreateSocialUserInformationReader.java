package com.eager.questioncloud.domain.user.implement;

import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.repository.CreateSocialUserInformationRepository;
import com.eager.questioncloud.domain.user.vo.AccountType;
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
