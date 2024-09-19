package com.eager.questioncloud.user;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken, AccountType accountType);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
