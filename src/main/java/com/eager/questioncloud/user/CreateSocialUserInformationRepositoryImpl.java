package com.eager.questioncloud.user;

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
            .toDomain();
    }

    @Override
    public CreateSocialUserInformation append(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationJpaRepository.save(createSocialUserInformation.toEntity()).toDomain();
    }

    @Override
    public CreateSocialUserInformation save(CreateSocialUserInformation createSocialUserInformation) {
        return createSocialUserInformationJpaRepository.save(createSocialUserInformation.toEntity()).toDomain();
    }
}
