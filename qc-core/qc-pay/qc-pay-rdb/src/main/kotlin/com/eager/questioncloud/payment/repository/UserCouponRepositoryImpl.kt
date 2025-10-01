package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.UserCoupon
import com.eager.questioncloud.payment.dto.AvailableUserCoupon
import com.eager.questioncloud.payment.entity.QCouponEntity.couponEntity
import com.eager.questioncloud.payment.entity.QUserCouponEntity.userCouponEntity
import com.eager.questioncloud.payment.entity.UserCouponEntity
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
        return jpaQueryFactory.select(userCouponEntity)
            .from(userCouponEntity)
            .where(
                userCouponEntity.id.eq(userCouponId),
                userCouponEntity.userId.eq(userId),
                userCouponEntity.isUsed.isFalse,
                userCouponEntity.endAt.after(LocalDateTime.now())
            )
            .fetchFirst()
            ?.toModel() ?: throw CoreException(Error.WRONG_COUPON)
    }
    
    override fun getUserCoupon(userCouponId: Long): UserCoupon {
        return userCouponJpaRepository.findById(userCouponId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }
    
    override fun getUserCoupon(userCouponIds: List<Long>, userId: Long): List<UserCoupon> {
        val result = jpaQueryFactory.select(userCouponEntity)
            .from(userCouponEntity)
            .where(
                userCouponEntity.id.`in`(userCouponIds),
                userCouponEntity.userId.eq(userId),
                userCouponEntity.isUsed.isFalse,
                userCouponEntity.endAt.after(LocalDateTime.now())
            )
            .fetch()
            .map { it.toModel() }
        
        if (result.size != userCouponIds.size) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        return result
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
                couponEntity.discountCalculationType,
                couponEntity.targetQuestionId,
                couponEntity.targetCreatorId,
                couponEntity.targetCategoryId,
                couponEntity.minimumPurchaseAmount,
                couponEntity.maximumDiscountAmount,
                couponEntity.isDuplicable,
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
    override fun use(userCouponId: Long, orderId: String) {
        val result = jpaQueryFactory.update(userCouponEntity)
            .set(userCouponEntity.isUsed, true)
            .set(userCouponEntity.usedOrderId, orderId)
            .where(
                userCouponEntity.id.eq(userCouponId),
                userCouponEntity.isUsed.isFalse()
            )
            .execute() == 1L
        
        if (!result) {
            throw CoreException(Error.FAIL_USE_COUPON)
        }
    }
    
    override fun deleteAllInBatch() {
        userCouponJpaRepository.deleteAllInBatch()
    }
}
