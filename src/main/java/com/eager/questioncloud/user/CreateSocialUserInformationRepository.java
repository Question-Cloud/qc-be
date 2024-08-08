package com.eager.questioncloud.user;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken);

    CreateSocialUserInformation append(CreateSocialUserInformation createSocialUserInformation);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
