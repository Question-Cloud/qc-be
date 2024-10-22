package com.eager.questioncloud.domain.user.repository;

import com.eager.questioncloud.domain.user.model.CreateSocialUserInformation;
import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
        return createSocialUserInformationJpaRepository.save(createSocialUserInformation.toEntity()).toModel();
    }
}
