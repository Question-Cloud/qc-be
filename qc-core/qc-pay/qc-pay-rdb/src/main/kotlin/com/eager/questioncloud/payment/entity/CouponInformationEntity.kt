package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.CouponInformation
import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon")
class CouponInformationEntity(
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
    fun toDomain(): CouponInformation {
        return CouponInformation(
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
        fun from(couponInformation: CouponInformation): CouponInformationEntity {
            return CouponInformationEntity(
                couponInformation.id,
                couponInformation.code,
                couponInformation.title,
                couponInformation.couponType,
                couponInformation.discountCalculationType,
                couponInformation.targetQuestionId,
                couponInformation.targetCreatorId,
                couponInformation.targetCategoryId,
                couponInformation.value,
                couponInformation.minimumPurchaseAmount,
                couponInformation.maximumDiscountAmount,
                couponInformation.remainingCount,
                couponInformation.isDuplicable,
                couponInformation.endAt
            )
        }
    }
}
