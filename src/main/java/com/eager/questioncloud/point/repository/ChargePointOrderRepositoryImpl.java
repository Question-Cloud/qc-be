package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointOrder;
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
}
