package com.eager.questioncloud.application.api.payment.question.service

import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentCouponReader
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon
import org.springframework.stereotype.Service

@Service
class QuestionPaymentCouponService(
    private val questionPaymentCouponReader: QuestionPaymentCouponReader
) {
    fun getQuestionPaymentCoupon(userCouponId: Long?, userId: Long): QuestionPaymentCoupon? {
        return questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId)
    }
}
