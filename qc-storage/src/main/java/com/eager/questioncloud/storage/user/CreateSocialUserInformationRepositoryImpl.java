package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.core.domain.user.repository.CreateSocialUserInformationRepository;
import com.eager.questioncloud.core.domain.user.vo.AccountType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreateSocialUserInformationRepositoryImpl implements CreateSocialUserInformationRepository {
    private final CreateSocialUserInformationJpaRepository createSocialUserInformationJpaRepository;


    @Override
    public CreateSocialUserInformation find(String registerToken, AccountType accountType) {
        return createSocialUserInformationJpaRepository.findByRegisterTokenAndAccountTypeAndIsUsedFalse(registerToken, accountType)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationJpaRepository.save(CreateSocialUserInformationEntity.from(createSocialUserInformation)).toModel();
    }
}
