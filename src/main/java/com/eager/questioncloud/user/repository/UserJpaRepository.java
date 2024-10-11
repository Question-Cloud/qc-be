package com.eager.questioncloud.user.repository;

import com.eager.questioncloud.user.entity.UserEntity;
import com.eager.questioncloud.user.vo.AccountType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByAccountTypeAndSocialUid(AccountType accountType, String socialUid);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);

    Optional<UserEntity> findByAccountTypeAndSocialUid(AccountType accountType, String socialUid);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);
}
