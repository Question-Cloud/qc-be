package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserPointPaymentRepositoryImpl implements UserPointPaymentRepository {
    private final UserPointPaymentJpaRepository userPointPaymentJpaRepository;

    @Override
    public ChargePointHistory save(ChargePointHistory chargePointHistory) {
        return userPointPaymentJpaRepository.save(chargePointHistory.toEntity()).toModel();
    }

    @Override
    public Boolean existsById(String paymentId) {
        return userPointPaymentJpaRepository.existsById(paymentId);
    }
}
