package com.eager.questioncloud.coupon.infrastructure.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.coupon.domain.UserCoupon
import com.eager.questioncloud.coupon.dto.AvailableUserCoupon
import com.eager.questioncloud.coupon.infrastructure.entity.QCouponEntity.couponEntity
import com.eager.questioncloud.coupon.infrastructure.entity.QUserCouponEntity.userCouponEntity
import com.eager.questioncloud.coupon.infrastructure.entity.UserCouponEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class UserCouponRepositoryImpl(
    private val userCouponJpaRepository: UserCouponJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : UserCouponRepository {

    override fun getUserCoupon(userCouponId: Long, userId: Long): UserCoupon {
        return userCouponJpaRepository.findByIdAndUserIdAndIsUsedFalse(userCouponId, userId)
            .orElseThrow { CoreException(Error.WRONG_COUPON) }
            .toModel()
    }

    override fun getUserCoupon(userCouponId: Long): UserCoupon {
        return userCouponJpaRepository.findById(userCouponId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun isRegistered(userId: Long, couponId: Long): Boolean {
        return userCouponJpaRepository.existsByUserIdAndCouponId(userId, couponId)
    }

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponJpaRepository.save(UserCouponEntity.from(userCoupon)).toModel()
    }

    override fun getAvailableUserCoupons(userId: Long): List<AvailableUserCoupon> {
        return jpaQueryFactory.select(
            Projections.constructor(
                AvailableUserCoupon::class.java,
                userCouponEntity.id,
                couponEntity.title,
                couponEntity.couponType,
                couponEntity.value,
                userCouponEntity.endAt
            )
        )
            .from(userCouponEntity)
            .leftJoin(couponEntity).on(couponEntity.id.eq(userCouponEntity.couponId))
            .where(
                userCouponEntity.userId.eq(userId),
                userCouponEntity.isUsed.isFalse(),
                userCouponEntity.endAt.after(LocalDateTime.now())
            )
            .fetch()
    }

    @Transactional
    override fun use(userCouponId: Long): Boolean {
        return jpaQueryFactory.update(userCouponEntity)
            .set(userCouponEntity.isUsed, true)
            .where(
                userCouponEntity.id.eq(userCouponId),
                userCouponEntity.isUsed.isFalse()
            )
            .execute() == 1L
    }

    override fun deleteAllInBatch() {
        userCouponJpaRepository.deleteAllInBatch()
    }
}
