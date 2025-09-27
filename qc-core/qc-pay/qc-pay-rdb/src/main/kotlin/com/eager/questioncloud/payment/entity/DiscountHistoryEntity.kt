package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.domain.DiscountType
import jakarta.persistence.*

@Entity
@Table(name = "discount_history")
class DiscountHistoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column val orderId: String,
    @Enumerated(EnumType.STRING) @Column val discountType: DiscountType,
    @Column val appliedAmount: Int,
    @Column val name: String,
    @Column val sourceId: Long,
) {
    companion object {
        fun from(discountHistory: DiscountHistory): DiscountHistoryEntity {
            return DiscountHistoryEntity(
                discountHistory.id,
                discountHistory.orderId,
                discountHistory.discountType,
                discountHistory.appliedAmount,
                discountHistory.name,
                discountHistory.sourceId,
            )
        }
    }
}