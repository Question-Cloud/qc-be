package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChargePointPaymentRepositoryImpl implements ChargePointPaymentRepository {
    private final ChargePointPaymentJpaRepository chargePointPaymentJpaRepository;

    @Override
    public ChargePointPayment save(ChargePointPayment chargePointPayment) {
        return chargePointPaymentJpaRepository.save(chargePointPayment.toEntity()).toModel();
    }

    @Override
    public Boolean existsById(String paymentId) {
        return chargePointPaymentJpaRepository.existsById(paymentId);
    }
}
