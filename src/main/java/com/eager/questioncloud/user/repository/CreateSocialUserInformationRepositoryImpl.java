package com.eager.questioncloud.user.repository;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.domain.CreateSocialUserInformation;
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
            .toDomain();
    }

    @Override
    public CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationJpaRepository.save(createSocialUserInformation.toEntity()).toDomain();
    }
}
