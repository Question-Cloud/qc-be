package com.eager.questioncloud.core.domain.user.infrastructure.repository;

import com.eager.questioncloud.core.domain.user.enums.AccountType;
import com.eager.questioncloud.core.domain.user.infrastructure.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserAccountInformationEntityAccountTypeAndUserAccountInformationEntitySocialUid(AccountType accountType, String socialUid);

    Boolean existsByUserInformationEntityPhone(String phone);

    Boolean existsByUserInformationEntityEmail(String email);

    Optional<UserEntity> findByUserAccountInformationEntityAccountTypeAndUserAccountInformationEntitySocialUid(AccountType accountType,
        String socialUid);

    Optional<UserEntity> findByUserInformationEntityEmail(String email);

    Optional<UserEntity> findByUserInformationEntityPhone(String phone);
}
