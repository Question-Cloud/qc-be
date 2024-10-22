package com.eager.questioncloud.domain.user.implement;

import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.repository.CreateSocialUserInformationRepository;
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
