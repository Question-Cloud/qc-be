package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.infrastructure.entity.CouponEntity
import com.eager.questioncloud.payment.infrastructure.entity.QCouponEntity.couponEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : CouponRepository {

    override fun findById(id: Long): Coupon {
        return couponJpaRepository.findById(id)
            .orElseThrow { CoreException(Error.WRONG_COUPON) }
            .toDomain()
    }

    override fun findByCode(code: String): Coupon {
        return couponJpaRepository.findByCode(code)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toDomain()
    }

    override fun save(coupon: Coupon): Coupon {
        return couponJpaRepository.save(CouponEntity.from(coupon)).toDomain()
    }

    override fun decreaseCount(couponId: Long): Boolean {
        val result = jpaQueryFactory.update(couponEntity)
            .set(
                couponEntity.remainingCount,
                couponEntity.remainingCount.subtract(1)
            )
            .where(couponEntity.id.eq(couponId), couponEntity.remainingCount.goe(1))
            .execute()
        return result == 1L
    }

    override fun deleteAllInBatch() {
        couponJpaRepository.deleteAllInBatch()
    }
}
