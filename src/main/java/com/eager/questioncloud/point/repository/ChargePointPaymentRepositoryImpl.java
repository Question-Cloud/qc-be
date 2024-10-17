package com.eager.questioncloud.point.repository;

import static com.eager.questioncloud.point.entity.QChargePointPaymentEntity.chargePointPaymentEntity;

import com.eager.questioncloud.point.model.ChargePointPayment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChargePointPaymentRepositoryImpl implements ChargePointPaymentRepository {
    private final ChargePointPaymentJpaRepository chargePointPaymentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ChargePointPayment save(ChargePointPayment chargePointPayment) {
        return chargePointPaymentJpaRepository.save(chargePointPayment.toEntity()).toModel();
    }

    @Override
    public Boolean existsByUserIdAndPaymentId(Long userId, String paymentId) {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId)
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.paymentId.eq(paymentId), chargePointPaymentEntity.userId.eq(userId))
            .fetchFirst() != null;
    }
}
