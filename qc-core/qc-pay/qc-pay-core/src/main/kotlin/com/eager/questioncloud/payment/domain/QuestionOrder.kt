package com.eager.questioncloud.payment.domain

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import io.hypersistence.tsid.TSID

class QuestionOrder(
    val orderId: String,
    val items: List<QuestionOrderItem>
) {
    val totalOriginalPrice: Int
        get() = items.sumOf { it.originalPrice }
    
    val totalPrice: Int
        get() = items.sumOf { it.realPrice }
    
    val questionIds: List<Long>
        get() = items.map { it.questionInfo.questionId }
    
    val orderDiscount: List<DiscountHistory>
        get() = items.flatMap { it.orderDiscountHistories }
    
    fun getOrderItem(questionId: Long): QuestionOrderItem {
        val target = items.find { it.questionInfo.questionId == questionId }
        
        if (target == null) {
            throw CoreException(Error.PAYMENT_ERROR)
        }
        
        return target
    }
    
    companion object {
        fun createOrder(orderItems: List<QuestionOrderItem>): QuestionOrder {
            val orderId = TSID.Factory.getTsid().toString()
            return QuestionOrder(orderId, orderItems)
        }
    }
}
