package com.eager.questioncloud.payment.question.dto

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.validator.Validatable
import com.eager.questioncloud.payment.question.command.QuestionOrderCommand

class QuestionPaymentRequest(
    val orders: List<QuestionOrderRequest>,
    val userCouponId: Long?,
) : Validatable {
    init {
        validate()
    }
    
    override fun validate() {
        if (orders.isEmpty()) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }
}

class QuestionOrderRequest(
    val questionId: Long,
    val userCouponIds: List<Long> = listOf(),
) {
    fun toCommand(): QuestionOrderCommand {
        return QuestionOrderCommand(questionId, userCouponIds)
    }
}