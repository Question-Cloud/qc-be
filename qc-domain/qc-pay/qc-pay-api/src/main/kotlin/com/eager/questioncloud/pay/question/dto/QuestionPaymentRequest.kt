package com.eager.questioncloud.pay.question.dto

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.validator.Validatable

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
