package com.eager.questioncloud.pay.question.service

import com.eager.questioncloud.pay.question.implement.QuestionPaymentCouponReader
import com.eager.questioncloud.payment.domain.QuestionPaymentCoupon
import org.springframework.stereotype.Service

@Service
class QuestionPaymentCouponService(
    private val questionPaymentCouponReader: QuestionPaymentCouponReader
) {
    fun getQuestionPaymentCoupon(userCouponId: Long?, userId: Long): QuestionPaymentCoupon? {
        return questionPaymentCouponReader.getQuestionPaymentCoupon(userCouponId, userId)
    }
}
