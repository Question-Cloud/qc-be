package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreateSocialUserInformationJpaRepository extends JpaRepository<CreateSocialUserInformationEntity, String> {
    Optional<CreateSocialUserInformationEntity> findByRegisterTokenAndAccountTypeAndIsUsedFalse(String registerToken, AccountType accountType);
}
