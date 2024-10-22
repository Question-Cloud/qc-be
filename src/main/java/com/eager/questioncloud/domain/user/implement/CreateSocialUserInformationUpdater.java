package com.eager.questioncloud.domain.user.implement;

import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.repository.CreateSocialUserInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSocialUserInformationUpdater {
    private final CreateSocialUserInformationRepository createSocialUserInformationRepository;

    public void use(CreateSocialUserInformation createSocialUserInformation) {
        createSocialUserInformation.use();
        createSocialUserInformationRepository.save(createSocialUserInformation);
    }
}
