package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.domain.QuestionOrderItem
import jakarta.persistence.*

@Entity
@Table(name = "question_order")
class QuestionOrderEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var orderId: String,
    @Column var questionId: Long,
    @Column var originalPrice: Int,
    @Column var realPrice: Int,
    @Column var promotionId: Long? = null,
    @Column var promotionName: String? = null,
    @Column var promotionDiscountAmount: Int = 0,
) {
    companion object {
        fun from(questionOrder: QuestionOrder): List<QuestionOrderEntity> {
            return questionOrder.items
                .stream()
                .map { item: QuestionOrderItem ->
                    QuestionOrderEntity(
                        item.id,
                        questionOrder.orderId,
                        item.questionId,
                        item.originalPrice,
                        item.realPrice,
                        item.promotionId,
                        item.promotionName,
                        item.promotionDiscountAmount
                    )
                }
                .toList()
        }
    }
}
