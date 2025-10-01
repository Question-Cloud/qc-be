package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.CouponInformation
import com.eager.questioncloud.payment.entity.CouponInformationEntity
import com.eager.questioncloud.payment.entity.QCouponInformationEntity.couponInformationEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CouponInformationRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : CouponInformationRepository {
    
    override fun findById(id: Long): CouponInformation {
        return couponJpaRepository.findById(id)
            .orElseThrow { CoreException(Error.WRONG_COUPON) }
            .toDomain()
    }
    
    override fun findByIdIn(ids: List<Long>): List<CouponInformation> {
        return jpaQueryFactory.select(couponInformationEntity)
            .from(couponInformationEntity)
            .where(couponInformationEntity.id.`in`(ids))
            .fetch()
            .map { it.toDomain() }
    }
    
    override fun findByCode(code: String): CouponInformation {
        return couponJpaRepository.findByCode(code)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toDomain()
    }
    
    override fun save(couponInformation: CouponInformation): CouponInformation {
        return couponJpaRepository.save(CouponInformationEntity.from(couponInformation)).toDomain()
    }
    
    override fun decreaseCount(couponId: Long): Boolean {
        val result = jpaQueryFactory.update(couponInformationEntity)
            .set(
                couponInformationEntity.remainingCount,
                couponInformationEntity.remainingCount.subtract(1)
            )
            .where(couponInformationEntity.id.eq(couponId), couponInformationEntity.remainingCount.goe(1))
            .execute()
        return result == 1L
    }
    
    override fun deleteAllInBatch() {
        couponJpaRepository.deleteAllInBatch()
    }
}
