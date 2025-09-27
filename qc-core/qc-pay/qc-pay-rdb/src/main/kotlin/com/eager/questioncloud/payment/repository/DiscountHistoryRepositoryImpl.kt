package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.entity.DiscountHistoryEntity
import org.springframework.stereotype.Repository

@Repository
class DiscountHistoryRepositoryImpl(
    private val discountHistoryJpaRepository: DiscountHistoryJpaRepository
) : DiscountHistoryRepository {
    override fun saveAll(discountHistory: List<DiscountHistory>) {
        discountHistoryJpaRepository.saveAll(discountHistory.map { DiscountHistoryEntity.from(it) })
    }
    
    override fun findByOrderId(orderId: String): List<DiscountHistory> {
        return discountHistoryJpaRepository.findByOrderId(orderId)
            .map { it.toModel() }
    }
}