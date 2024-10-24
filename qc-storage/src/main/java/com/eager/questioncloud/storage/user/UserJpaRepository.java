package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.user.vo.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserAccountInformationAccountTypeAndUserAccountInformationSocialUid(AccountType accountType, String socialUid);

    Boolean existsByUserInformationPhone(String phone);

    Boolean existsByUserInformationEmail(String email);

    Optional<UserEntity> findByUserAccountInformationAccountTypeAndUserAccountInformationSocialUid(AccountType accountType, String socialUid);

    Optional<UserEntity> findByUserInformationEmail(String email);

    Optional<UserEntity> findByUserInformationPhone(String phone);
}
