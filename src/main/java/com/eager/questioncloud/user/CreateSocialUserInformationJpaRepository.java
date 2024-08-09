package com.eager.questioncloud.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateSocialUserInformationJpaRepository extends JpaRepository<CreateSocialUserInformationEntity, String> {
    Optional<CreateSocialUserInformationEntity> findByRegisterTokenAndIsUsedFalse(String registerToken);
}
