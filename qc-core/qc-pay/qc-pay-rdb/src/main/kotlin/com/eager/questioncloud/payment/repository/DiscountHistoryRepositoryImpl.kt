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
    
    override fun findByPaymentId(paymentId: Long): List<DiscountHistory> {
        return discountHistoryJpaRepository.findByPaymentId(paymentId)
            .map { it.toModel() }
    }
}