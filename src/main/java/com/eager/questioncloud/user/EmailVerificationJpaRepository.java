package com.eager.questioncloud.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationEntity, String> {
}
