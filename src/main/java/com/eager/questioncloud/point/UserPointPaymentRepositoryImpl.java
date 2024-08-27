package com.eager.questioncloud.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointPaymentRepositoryImpl implements UserPointPaymentRepository {
    private final UserPointPaymentJpaRepository userPointPaymentJpaRepository;

    @Override
    public UserPointPayment append(UserPointPayment userPointPayment) {
        return userPointPaymentJpaRepository.save(userPointPayment.toEntity()).toModel();
    }

    @Override
    public Boolean existsById(String paymentId) {
        return userPointPaymentJpaRepository.existsById(paymentId);
    }
}
