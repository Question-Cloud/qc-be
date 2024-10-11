package com.eager.questioncloud.user.repository;

import com.eager.questioncloud.user.domain.AccountType;
import com.eager.questioncloud.user.entity.CreateSocialUserInformationEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateSocialUserInformationJpaRepository extends JpaRepository<CreateSocialUserInformationEntity, String> {
    Optional<CreateSocialUserInformationEntity> findByRegisterTokenAndAccountTypeAndIsUsedFalse(String registerToken, AccountType accountType);
}
