package com.eager.questioncloud.domain.point.repository;

import com.eager.questioncloud.domain.point.entity.ChargePointOrderEntity;
import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChargePointOrderRepositoryImpl implements ChargePointOrderRepository {
    private final ChargePointOrderJpaRepository chargePointOrderJpaRepository;

    @Override
    public ChargePointOrder append(ChargePointOrder chargePointOrder) {
        return chargePointOrderJpaRepository.save(chargePointOrder.toEntity()).toModel();
    }

    @Override
    public ChargePointOrder findByPaymentId(String paymentId) {
        return chargePointOrderJpaRepository.findByPaymentId(paymentId)
            .map(ChargePointOrderEntity::toModel)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
    }

    @Override
    public ChargePointOrder save(ChargePointOrder chargePointOrder) {
        return chargePointOrderJpaRepository.save(chargePointOrder.toEntity()).toModel();
    }
}
