package com.eager.questioncloud.core.domain.point.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity
import com.eager.questioncloud.core.domain.point.infrastructure.entity.ChargePointPaymentEntity.Companion.from
import com.eager.questioncloud.core.domain.point.infrastructure.entity.QChargePointPaymentEntity.chargePointPaymentEntity
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class ChargePointPaymentRepositoryImpl(
    private val chargePointPaymentJpaRepository: ChargePointPaymentJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ChargePointPaymentRepository {
    override fun save(chargePointPayment: ChargePointPayment): ChargePointPayment {
        return chargePointPaymentJpaRepository.save(from(chargePointPayment)).toModel()
    }

    override fun isCompletedPayment(userId: Long, paymentId: String): Boolean {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId)
            .from(chargePointPaymentEntity)
            .where(
                chargePointPaymentEntity.paymentId.eq(paymentId),
                chargePointPaymentEntity.userId.eq(userId),
                chargePointPaymentEntity.chargePointPaymentStatus.eq(ChargePointPaymentStatus.PAID)
            )
            .fetchFirst() != null
    }

    override fun findByPaymentId(paymentId: String): ChargePointPayment {
        return chargePointPaymentJpaRepository.findByPaymentId(paymentId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun findByPaymentIdWithLock(paymentId: String): ChargePointPayment {
        return chargePointPaymentJpaRepository.findByPaymentIdWithLock(paymentId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getChargePointPayments(userId: Long, pagingInformation: PagingInformation): List<ChargePointPayment> {
        return jpaQueryFactory.select(chargePointPaymentEntity)
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.userId.eq(userId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .orderBy(chargePointPaymentEntity.paymentId.desc())
            .fetch()
            .stream()
            .map { entity: ChargePointPaymentEntity -> entity.toModel() }
            .collect(Collectors.toList())
    }

    override fun countByUserId(userId: Long): Int {
        return jpaQueryFactory.select(chargePointPaymentEntity.paymentId.count())
            .from(chargePointPaymentEntity)
            .where(chargePointPaymentEntity.userId.eq(userId))
            .fetchFirst()?.toInt() ?: 0
    }

    override fun deleteAllInBatch() {
        chargePointPaymentJpaRepository.deleteAllInBatch()
    }
}
