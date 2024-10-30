package com.eager.questioncloud.storage.user;

import com.eager.questioncloud.core.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.point.repository.ChargePointOrderRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChargePointOrderRepositoryImpl implements ChargePointOrderRepository {
    private final ChargePointOrderJpaRepository chargePointOrderJpaRepository;

    @Override
    public ChargePointOrder append(ChargePointOrder chargePointOrder) {
        return chargePointOrderJpaRepository.save(ChargePointOrderEntity.from(chargePointOrder)).toModel();
    }

    @Override
    public ChargePointOrder findByPaymentId(String paymentId) {
        return chargePointOrderJpaRepository.findByPaymentId(paymentId)
            .map(ChargePointOrderEntity::toModel)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND));
    }

    @Override
    public ChargePointOrder save(ChargePointOrder chargePointOrder) {
        return chargePointOrderJpaRepository.save(ChargePointOrderEntity.from(chargePointOrder)).toModel();
    }
}
