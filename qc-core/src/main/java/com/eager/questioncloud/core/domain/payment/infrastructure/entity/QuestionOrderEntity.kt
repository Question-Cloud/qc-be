package com.eager.questioncloud.core.domain.payment.infrastructure.entity

import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import com.eager.questioncloud.core.domain.payment.model.QuestionOrderItem
import jakarta.persistence.*

@Entity
@Table(name = "question_order")
class QuestionOrderEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long?,
    @Column var orderId: String,
    @Column var questionId: Long,
    @Column var price: Int
) {
    companion object {
        @JvmStatic
        fun from(questionOrder: QuestionOrder): List<QuestionOrderEntity> {
            return questionOrder.items
                .stream()
                .map { item: QuestionOrderItem ->
                    QuestionOrderEntity(item.id, questionOrder.orderId, item.questionId, item.price)
                }
                .toList()
        }
    }
}
