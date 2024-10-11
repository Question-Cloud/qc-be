package com.eager.questioncloud.user.implement;

import com.eager.questioncloud.user.domain.CreateSocialUserInformation;
import com.eager.questioncloud.user.repository.CreateSocialUserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationAppender {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public CreateSocialUserInformation append(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationRepository.save(createSocialUserInformation);
    }
}
