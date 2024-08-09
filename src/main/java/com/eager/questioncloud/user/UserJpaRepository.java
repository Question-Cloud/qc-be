package com.eager.questioncloud.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByLoginId(String loginId);

    Boolean existsByAccountTypeAndSocialUid(AccountType accountType, String socialUid);

    Boolean existsByPhone(String phone);

    Boolean existsByEmail(String email);

    // Todo 리턴 타입 변경 User -> UserEntity
    Optional<User> findByAccountTypeAndSocialUid(AccountType accountType, String socialUid);

    Optional<UserEntity> findByLoginId(String loginId);
}
