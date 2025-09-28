package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.Promotion

interface PromotionRepository {
    fun save(promotion: Promotion): Promotion
    
    fun findByQuestionIdIn(questionIds: List<Long>): List<Promotion>
}