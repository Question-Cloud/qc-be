package com.eager.questioncloud.user.repository;

import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.CreateSocialUserInformation;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken, AccountType accountType);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
