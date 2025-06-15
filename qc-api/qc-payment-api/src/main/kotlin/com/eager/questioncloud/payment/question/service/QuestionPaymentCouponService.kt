package com.eager.questioncloud.payment.question.service

import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import com.eager.questioncloud.payment.question.implement.QuestionPaymentCouponReader
import org.springframework.stereotype.Service

@Service
class QuestionPaymentCouponService(
    private val questionPaymentCouponReader: QuestionPaymentCouponReader
) {
    fun getQuestionPaymentCoupon(userCouponId: Long?, userId: Long): QuestionPaymentCoupon? {
        return questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId)
    }
}
