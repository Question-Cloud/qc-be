package com.eager.questioncloud.payment.question.implement

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.repository.DiscountHistoryRepository
import com.eager.questioncloud.payment.repository.QuestionOrderRepository
import com.eager.questioncloud.payment.repository.QuestionPaymentRepository
import org.springframework.stereotype.Component

@Component
class QuestionPaymentRecorder(
    private val questionPaymentRepository: QuestionPaymentRepository,
    private val questionOrderRepository: QuestionOrderRepository,
    private val discountHistoryRepository: DiscountHistoryRepository,
) {
    fun record(questionPayment: QuestionPayment) {
        questionPaymentRepository.save(questionPayment)
        questionOrderRepository.save(questionPayment.order)
        
        val discountHistories = mutableListOf<DiscountHistory>()
        
        questionPayment.appliedPaymentCoupons.forEach { appliedPaymentCoupon ->
            discountHistories.add(
                DiscountHistory(
                    couponType = appliedPaymentCoupon.couponInformation.couponType,
                    paymentId = questionPayment.paymentId,
                    discountAmount = appliedPaymentCoupon.appliedDiscountAmount,
                    name = appliedPaymentCoupon.getName(),
                    sourceId = appliedPaymentCoupon.getSourceId()
                )
            )
        }
        questionPayment.order.items.forEach {
            if (it.appliedCoupons.isEmpty()) return@forEach
            it.appliedCoupons.forEach { appliedCoupon ->
                discountHistories.add(
                    DiscountHistory(
                        couponType = appliedCoupon.couponInformation.couponType,
                        paymentId = questionPayment.paymentId,
                        orderItemId = it.id,
                        discountAmount = appliedCoupon.appliedDiscountAmount,
                        name = appliedCoupon.getName(),
                        sourceId = appliedCoupon.getSourceId()
                    )
                )
            }
        }
        discountHistoryRepository.saveAll(discountHistories)
    }
}