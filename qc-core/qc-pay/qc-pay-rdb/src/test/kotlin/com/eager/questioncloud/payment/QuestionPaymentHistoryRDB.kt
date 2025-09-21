package com.eager.questioncloud.payment

import com.eager.questioncloud.payment.domain.QuestionPaymentHistory

class QuestionPaymentHistoryRDB(
    val orderId: String,
    val userId: Long,
    val history: QuestionPaymentHistory,
) {
}