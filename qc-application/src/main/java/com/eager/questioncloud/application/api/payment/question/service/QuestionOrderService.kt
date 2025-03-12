package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.api.payment.question.implement.QuestionOrderGenerator
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import org.springframework.stereotype.Service

@Service
class QuestionOrderService(
    private val questionOrderGenerator: QuestionOrderGenerator
) {
    fun generateQuestionOrder(userId: Long, questionIds: List<Long>): QuestionOrder {
        return questionOrderGenerator.generateQuestionOrder(userId, questionIds)
    }
}
