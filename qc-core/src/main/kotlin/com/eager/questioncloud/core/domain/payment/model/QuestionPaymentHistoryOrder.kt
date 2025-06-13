package com.eager.questioncloud.core.domain.payment.model

import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import com.eager.questioncloud.core.domain.question.enums.Subject

class QuestionPaymentHistoryOrder(
    val questionId: Long,
    val amount: Int,
    val title: String,
    val thumbnail: String,
    val creatorName: String,
    val subject: Subject,
    val mainCategory: String,
    val subCategory: String,
) {
    companion object {
        fun from(questionInformation: QuestionInformation): QuestionPaymentHistoryOrder {
            return QuestionPaymentHistoryOrder(
                questionInformation.id,
                questionInformation.price,
                questionInformation.title,
                questionInformation.thumbnail,
                "temp",
                questionInformation.subject,
                questionInformation.parentCategory,
                questionInformation.childCategory
            )
        }
    }
}