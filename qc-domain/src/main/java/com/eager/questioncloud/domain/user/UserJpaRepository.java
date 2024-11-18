package com.eager.questioncloud.domain.user;

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
