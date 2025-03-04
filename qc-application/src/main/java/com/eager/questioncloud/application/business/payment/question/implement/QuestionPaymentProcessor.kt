package com.eager.questioncloud.application.business.payment.question.implement

import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import com.eager.questioncloud.core.domain.point.implement.UserPointManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionPaymentProcessor(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    private val userPointManager: UserPointManager,
) {
    @Transactional
    fun payment(questionPayment: QuestionPayment): QuestionPayment {
        questionPaymentCouponProcessor.applyCoupon(questionPayment)
        userPointManager.usePoint(questionPayment.userId, questionPayment.amount)
        return questionPaymentRepository.save(questionPayment)
    }
}
