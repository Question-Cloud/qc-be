package com.eager.questioncloud.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByLoginId(String loginId);

    Boolean existsByAccountTypeAndSocialUid(AccountType accountType, String socialUid);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);
}
