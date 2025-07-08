package com.eager.questioncloud.pay.question.service

import com.eager.questioncloud.pay.question.implement.QuestionOrderGenerator
import com.eager.questioncloud.payment.domain.QuestionOrder
import org.springframework.stereotype.Service

@Service
class QuestionOrderService(
    private val questionOrderGenerator: QuestionOrderGenerator
) {
    fun generateQuestionOrder(userId: Long, questionIds: List<Long>): QuestionOrder {
        return questionOrderGenerator.generateQuestionOrder(userId, questionIds)
    }
}
