package com.eager.questioncloud.core.domain.point.infrastructure.repository;

import static com.eager.questioncloud.core.domain.point.infrastructure.entity.QChargePointPaymentEntity.chargePointPaymentEntity;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
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
        return chargePointPaymentJpaRepository.findByPaymentId(paymentId)
            .orElseThrow(() -> new CoreException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public Boolean existsByPaymentId(String paymentId) {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId)
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.paymentId.eq(paymentId))
            .fetchFirst() != null;
    }

    @Override
    public List<ChargePointPayment> getChargePointPayments(Long userId, PagingInformation pagingInformation) {
        return jpaQueryFactory.select(chargePointPaymentEntity)
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.userId.eq(userId))
            .offset(pagingInformation.getOffset())
            .limit(pagingInformation.getSize())
            .orderBy(chargePointPaymentEntity.id.desc())
            .fetch()
            .stream()
            .map(ChargePointPaymentEntity::toModel)
            .collect(Collectors.toList());
    }

    @Override
    public int countByUserId(Long userId) {
        return jpaQueryFactory.select(chargePointPaymentEntity.id.count())
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.userId.eq(userId))
            .fetchFirst()
            .intValue();
    }

    @Override
    public void deleteAllInBatch() {
        chargePointPaymentJpaRepository.deleteAllInBatch();
    }
}
