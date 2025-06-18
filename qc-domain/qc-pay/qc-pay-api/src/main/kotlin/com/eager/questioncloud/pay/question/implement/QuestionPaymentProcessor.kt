package com.eager.questioncloud.pay.question.implement

import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.infrastructure.repository.QuestionPaymentRepository
import com.eager.questioncloud.point.api.internal.PointCommandAPI
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class QuestionPaymentProcessor(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionPaymentCouponProcessor: QuestionPaymentCouponProcessor,
    private val pointCommandAPI: PointCommandAPI
) {
    @Transactional
    fun payment(questionPayment: QuestionPayment) {
        questionPaymentCouponProcessor.applyCoupon(questionPayment)
        pointCommandAPI.usePoint(questionPayment.userId, questionPayment.amount)
        questionPaymentRepository.save(questionPayment)
    }
}
