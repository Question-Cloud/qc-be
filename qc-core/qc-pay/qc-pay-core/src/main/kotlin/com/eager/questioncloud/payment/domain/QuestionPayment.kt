package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.event.DiscountInformation
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPayment(
    var order: QuestionOrder,
    var userId: Long,
    var discountPolicy: MutableList<DiscountPolicy> = mutableListOf(),
    var originalAmount: Int,
    var realAmount: Int,
    var status: QuestionPaymentStatus = QuestionPaymentStatus.SUCCESS,
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun applyDiscountPolicy() {
        realAmount -= discountPolicy.sumOf { it.getDiscountAmount(originalAmount) }
        
        if (realAmount < 0) {
            throw CoreException(Error.WRONG_COUPON)
        }
    }
    
    fun getDiscountInformation(): List<DiscountInformation> {
        return discountPolicy.map { DiscountInformation(it.getPolicyName(), it.getDiscountAmount(originalAmount)) }
    }
    
    companion object {
        fun payment(
            userId: Long,
            coupon: DiscountPolicy,
            order: QuestionOrder
        ): QuestionPayment {
            val payment = QuestionPayment(
                order = order,
                userId = userId,
                originalAmount = order.calcAmount(),
                realAmount = order.calcAmount()
            )
            payment.discountPolicy.add(coupon)
            payment.applyDiscountPolicy()
            return payment
        }
    }
}