package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.DiscountHistory

interface DiscountHistoryRepository {
    fun saveAll(discountHistory: List<DiscountHistory>)
    fun findByOrderId(orderId: String): List<DiscountHistory>
}