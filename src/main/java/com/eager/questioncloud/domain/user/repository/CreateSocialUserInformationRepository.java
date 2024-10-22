package com.eager.questioncloud.domain.user.repository;

import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.vo.AccountType;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken, AccountType accountType);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
