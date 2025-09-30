package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.DiscountHistory

interface DiscountHistoryRepository {
    fun saveAll(discountHistory: List<DiscountHistory>)
    fun findByPaymentId(paymentId: Long): List<DiscountHistory>
}