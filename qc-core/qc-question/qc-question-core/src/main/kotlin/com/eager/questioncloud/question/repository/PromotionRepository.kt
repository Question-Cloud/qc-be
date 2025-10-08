package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.domain.Promotion

interface PromotionRepository {
    fun save(promotion: Promotion): Promotion
    
    fun findByQuestionIdIn(questionIds: List<Long>): List<Promotion>
}