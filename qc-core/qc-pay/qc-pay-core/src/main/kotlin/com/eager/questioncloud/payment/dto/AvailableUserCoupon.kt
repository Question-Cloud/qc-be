package com.eager.questioncloud.payment.dto

import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import java.time.LocalDateTime

data class AvailableUserCoupon(
    val id: Long,
    val title: String,
    val couponType: CouponType,
    val discountCalculationType: DiscountCalculationType,
    val targetQuestionId: Long? = null,
    val targetCreatorId: Long? = null,
    val targetCategoryId: Long? = null,
    val minimumPurchaseAmount: Int,
    val maximumDiscountAmount: Int,
    val isDuplicable: Boolean,
    val value: Int,
    val endAt: LocalDateTime
)
