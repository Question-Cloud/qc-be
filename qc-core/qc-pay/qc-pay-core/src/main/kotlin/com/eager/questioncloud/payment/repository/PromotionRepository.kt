package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.Promotion

interface PromotionRepository {
    fun findByQuestionIdIn(questionIds: List<Long>): List<Promotion>
}