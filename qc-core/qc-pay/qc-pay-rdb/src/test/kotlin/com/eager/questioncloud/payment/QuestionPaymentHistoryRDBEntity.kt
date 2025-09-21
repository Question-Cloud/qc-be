package com.eager.questioncloud.payment

import com.eager.questioncloud.common.entity.BaseCustomIdEntity
import com.eager.questioncloud.payment.converter.PaymentHistoryConverter
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import jakarta.persistence.*

@Entity
@Table(name = "payment_history")
class QuestionPaymentHistoryRDBEntity(
    @Id val orderId: String,
    @Column val userId: Long,
    @Convert(converter = PaymentHistoryConverter::class) @Column(columnDefinition = "TEXT") val history: QuestionPaymentHistory,
    isNewEntity: Boolean
) : BaseCustomIdEntity<String>(isNewEntity) {
    override fun getId(): String {
        return orderId
    }
    
    companion object {
        fun createNewEntity(history: QuestionPaymentHistory): QuestionPaymentHistoryRDBEntity {
            return QuestionPaymentHistoryRDBEntity(history.orderId, history.userId, history, true)
        }
    }
}