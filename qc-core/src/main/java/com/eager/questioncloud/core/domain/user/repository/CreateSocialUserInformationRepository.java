package com.eager.questioncloud.core.domain.user.repository;


import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.vo.AccountType;

public interface CreateSocialUserInformationRepository {
    CreateSocialUserInformation find(String registerToken, AccountType accountType);

    CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation);
}
