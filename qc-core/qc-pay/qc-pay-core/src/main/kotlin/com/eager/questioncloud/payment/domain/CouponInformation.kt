package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.payment.enums.CouponType
import com.eager.questioncloud.payment.enums.DiscountCalculationType
import java.time.LocalDateTime

class CouponInformation(
    val id: Long = 0,
    val code: String,
    val title: String,
    val couponType: CouponType,
    val discountCalculationType: DiscountCalculationType,
    val targetQuestionId: Long? = null,
    val targetCreatorId: Long? = null,
    val targetCategoryId: Long? = null,
    val value: Int,
    val minimumPurchaseAmount: Int,
    val maximumDiscountAmount: Int,
    val remainingCount: Int,
    val isDuplicable: Boolean,
    val endAt: LocalDateTime
)

