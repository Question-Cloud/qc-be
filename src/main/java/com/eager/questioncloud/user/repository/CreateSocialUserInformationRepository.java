package com.eager.questioncloud.user.repository;

import com.eager.questioncloud.user.domain.CreateSocialUserInformation;
import com.eager.questioncloud.user.vo.AccountType;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken, AccountType accountType);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
