package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.question.implement.QuestionOrderGenerator
import org.springframework.stereotype.Service

@Service
class QuestionOrderService(
    private val questionOrderGenerator: QuestionOrderGenerator
) {
    fun generateQuestionOrder(userId: Long, questionIds: List<Long>): QuestionOrder {
        return questionOrderGenerator.generateQuestionOrder(userId, questionIds)
    }
}
