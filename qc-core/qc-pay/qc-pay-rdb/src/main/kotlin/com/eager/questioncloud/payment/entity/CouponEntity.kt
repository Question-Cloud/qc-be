package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.Coupon
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class CouponEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column var code: String,
    @Column var title: String,
    @Column @Enumerated(EnumType.STRING) var couponType: CouponType,
    @Column @Enumerated(EnumType.STRING) var discountCalculationType: DiscountCalculationType,
    @Column val targetQuestionId: Long? = null,
    @Column val targetCreatorId: Long? = null,
    @Column val targetCategoryId: Long? = null,
    @Column var value: Int,
    @Column val minimumPurchaseAmount: Int,
    @Column val maximumDiscountAmount: Int,
    @Column var remainingCount: Int,
    @Column val isDuplicable: Boolean,
    @Column var endAt: LocalDateTime
) {
    fun toDomain(): Coupon {
        return Coupon(
            id,
            code,
            title,
            couponType,
            discountCalculationType,
            targetQuestionId,
            targetCreatorId,
            targetCategoryId,
            value,
            minimumPurchaseAmount,
            maximumDiscountAmount,
            remainingCount,
            isDuplicable,
            endAt
        )
    }
    
    companion object {
        fun from(coupon: Coupon): CouponEntity {
            return CouponEntity(
                coupon.id,
                coupon.code,
                coupon.title,
                coupon.couponType,
                coupon.discountCalculationType,
                coupon.targetQuestionId,
                coupon.targetCreatorId,
                coupon.targetCategoryId,
                coupon.value,
                coupon.minimumPurchaseAmount,
                coupon.maximumDiscountAmount,
                coupon.remainingCount,
                coupon.isDuplicable,
                coupon.endAt
            )
        }
    }
}
