package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationAppender {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation append(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationRepository.append(createSocialUserInformation);
    }
}
