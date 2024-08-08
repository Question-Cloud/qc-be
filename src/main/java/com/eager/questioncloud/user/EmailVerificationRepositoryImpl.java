package com.eager.questioncloud.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailVerificationRepositoryImpl implements EmailVerificationRepository {
    private final EmailVerificationJpaRepository emailVerificationJpaRepository;

    @Override
    public EmailVerification append(EmailVerification emailVerification) {
        return emailVerificationJpaRepository.save(emailVerification.toEntity()).toDomain();
    }
}
