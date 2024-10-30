package com.eager.questioncloud.storage.user;

import static com.eager.questioncloud.storage.user.QChargePointPaymentEntity.chargePointPaymentEntity;

import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.repository.ChargePointPaymentRepository;
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
        return chargePointPaymentJpaRepository.save(ChargePointPaymentEntity.from(chargePointPayment)).toModel();
    }

    @Override
    public Boolean existsByUserIdAndPaymentId(Long userId, String paymentId) {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId)
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.paymentId.eq(paymentId), chargePointPaymentEntity.userId.eq(userId))
            .fetchFirst() != null;
    }
}
