package com.eager.questioncloud.point.infrastructure.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.domain.ChargePointPayment
import com.eager.questioncloud.point.enums.ChargePointPaymentStatus
import com.eager.questioncloud.point.infrastructure.entity.ChargePointPaymentEntity
import com.eager.questioncloud.point.infrastructure.entity.QChargePointPaymentEntity.chargePointPaymentEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class ChargePointPaymentRepositoryImpl(
    private val chargePointPaymentJpaRepository: ChargePointPaymentJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : ChargePointPaymentRepository {
    override fun save(chargePointPayment: ChargePointPayment): ChargePointPayment {
        return chargePointPaymentJpaRepository.save(
            ChargePointPaymentEntity.createNewEntity(
                chargePointPayment
            )
        )
            .toModel()
    }
    
    override fun update(chargePointPayment: ChargePointPayment): ChargePointPayment {
        return chargePointPaymentJpaRepository.save(
            ChargePointPaymentEntity.fromExisting(
                chargePointPayment
            )
        ).toModel()
    }
    
    override fun isCompletedPayment(userId: Long, orderId: String): Boolean {
        return jpaQueryFactory.select(chargePointPaymentEntity.orderId)
            .from(chargePointPaymentEntity)
            .where(
                chargePointPaymentEntity.orderId.eq(orderId),
                chargePointPaymentEntity.userId.eq(userId),
                chargePointPaymentEntity.chargePointPaymentStatus.eq(ChargePointPaymentStatus.CHARGED)
            )
            .fetchFirst() != null
    }
    
    override fun findByOrderIdWithLock(orderId: String): ChargePointPayment {
        return chargePointPaymentJpaRepository.findByOrderIdWithLock(orderId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }
    
    override fun findByOrderId(orderId: String): ChargePointPayment {
        return chargePointPaymentJpaRepository.findById(orderId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }
    
    override fun getChargePointPayments(
        userId: Long,
        pagingInformation: PagingInformation
    ): List<ChargePointPayment> {
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
