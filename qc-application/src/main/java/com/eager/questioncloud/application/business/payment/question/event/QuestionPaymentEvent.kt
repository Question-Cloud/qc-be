package com.eager.questioncloud.application.business.payment.question.event

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment

class QuestionPaymentEvent(
    val questionPayment: QuestionPayment
)
