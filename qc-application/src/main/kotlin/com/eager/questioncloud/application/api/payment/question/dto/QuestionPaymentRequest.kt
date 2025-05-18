package com.eager.questioncloud.application.api.payment.question.dto

import com.eager.questioncloud.application.validator.Validatable
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error

class QuestionPaymentRequest(
    val questionIds: List<Long>?,
    val userCouponId: Long?,
) : Validatable {
    init {
        validate()
    }

    override fun validate() {
        if (questionIds.isNullOrEmpty()) {
            throw CoreException(Error.BAD_REQUEST)
        }
    }
}
