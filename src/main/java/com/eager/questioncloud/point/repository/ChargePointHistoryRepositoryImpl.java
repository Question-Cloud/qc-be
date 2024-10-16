package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChargePointHistoryRepositoryImpl implements ChargePointHistoryRepository {
    private final ChargePointHistoryJpaRepository chargePointHistoryJpaRepository;

    @Override
    public ChargePointHistory save(ChargePointHistory chargePointHistory) {
        return chargePointHistoryJpaRepository.save(chargePointHistory.toEntity()).toModel();
    }

    @Override
    public Boolean existsById(String paymentId) {
        return chargePointHistoryJpaRepository.existsById(paymentId);
    }
}
