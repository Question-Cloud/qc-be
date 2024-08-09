package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreateSocialUserInformationRepositoryImpl implements CreateSocialUserInformationRepository {
    private final CreateSocialUserInformationJpaRepository createSocialUserInformationJpaRepository;


    @Override
    public CreateSocialUserInformation find(String registerToken) {
        return createSocialUserInformationJpaRepository.findByRegisterTokenAndIsUsedFalse(registerToken)
            .orElseThrow(RuntimeException::new)
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
