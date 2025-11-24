package com.eager.questioncloud.payment.document

import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.github.jershell.kbson.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.sql.Timestamp
import java.time.ZoneId
import java.util.*

@Document(collection = "question_payment_history")
@Serializable
data class QuestionPaymentHistoryDocument(
    @Id @SerialName("_id") val orderId: String,
    val userId: Long,
    val orders: List<QuestionPaymentHistoryOrderDocument>,
    val discountInformation: List<SimpleDiscountHistoryDocument>,
    val originalAmount: Int,
    val realAmount: Int,
    @Serializable(with = DateSerializer::class) val createdAt: Date,
) {
    fun toModel(): QuestionPaymentHistory {
        return QuestionPaymentHistory(
            orderId,
            userId,
            orders.map { it.toModel() },
            discountInformation.map { it.toModel() },
            originalAmount,
            realAmount,
            createdAt.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime(),
        )
    }
    
    companion object {
        fun from(questionPaymentHistory: QuestionPaymentHistory): QuestionPaymentHistoryDocument {
            return QuestionPaymentHistoryDocument(
                questionPaymentHistory.orderId,
                questionPaymentHistory.userId,
                QuestionPaymentHistoryOrderDocument.from(questionPaymentHistory.orders),
                SimpleDiscountHistoryDocument.from(questionPaymentHistory.discountInformation),
                questionPaymentHistory.originalAmount,
                questionPaymentHistory.realAmount,
                Timestamp.valueOf(questionPaymentHistory.createdAt),
            )
        }
    }
}
