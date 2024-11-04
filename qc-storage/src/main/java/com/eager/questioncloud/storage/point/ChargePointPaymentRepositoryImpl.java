package com.eager.questioncloud.storage.point;

import static com.eager.questioncloud.storage.point.QChargePointPaymentEntity.chargePointPaymentEntity;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointPaymentStatus;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
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
    public Boolean isCompletedPayment(Long userId, String paymentId) {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId)
            .from(chargePointPaymentEntity)
            .where(
                chargePointPaymentEntity.paymentId.eq(paymentId),
                chargePointPaymentEntity.userId.eq(userId),
                chargePointPaymentEntity.chargePointPaymentStatus.eq(ChargePointPaymentStatus.PAID))
            .fetchFirst() != null;
    }

    @Override
    public ChargePointPayment findByPaymentId(String paymentId) {
        return chargePointPaymentJpaRepository.findById(paymentId)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }
}
