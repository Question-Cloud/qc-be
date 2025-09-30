package com.eager.questioncloud.payment.domain

import kotlin.math.floor

interface DiscountCalculatePolicy {
    fun calculateDiscountAmount(originalPrice: Int): Int
}

class FixedDiscountCalculatePolicy(
    private val value: Int
) : DiscountCalculatePolicy {
    override fun calculateDiscountAmount(originalPrice: Int): Int {
        return value
    }
}

class PercentDiscountCalculatePolicy(
    private val value: Int
) : DiscountCalculatePolicy {
    override fun calculateDiscountAmount(originalPrice: Int): Int {
        val discountRate = value / 100.0
        val discountAmount = floor(originalPrice * discountRate).toInt()
        return discountAmount
    }
}
