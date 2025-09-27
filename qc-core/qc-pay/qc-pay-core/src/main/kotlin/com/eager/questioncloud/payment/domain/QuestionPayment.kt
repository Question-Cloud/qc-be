package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.event.DiscountInformation
import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import java.time.LocalDateTime

class QuestionPayment(
    var order: QuestionOrder,
    var userId: Long,
    var appliedDiscountList: MutableList<Discountable> = mutableListOf(),
    var originalAmount: Int,
    var realAmount: Int,
    var status: QuestionPaymentStatus = QuestionPaymentStatus.SUCCESS,
    var createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun applyDiscount(policy: Discountable) {
        realAmount -= policy.getDiscountAmount(order.getCurrentPrice())
        
        if (realAmount < 0) {
            throw CoreException(Error.WRONG_COUPON)
        }
        
        appliedDiscountList.add(policy)
    }
    
    fun getDiscountInformation(): List<DiscountInformation> {
        return appliedDiscountList.map { DiscountInformation(it.getName(), it.getDiscountAmount(order.getCurrentPrice())) }
    }
    
    companion object {
        fun create(
            userId: Long,
            order: QuestionOrder
        ): QuestionPayment {
            return QuestionPayment(
                order = order,
                userId = userId,
                originalAmount = order.getOriginalPrice(),
                realAmount = order.getCurrentPrice()
            )
        }
    }
}