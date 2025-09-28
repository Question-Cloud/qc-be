package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.enums.DiscountType
import jakarta.persistence.*

@Entity
@Table(name = "discount_history")
class DiscountHistoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column val orderId: String,
    @Enumerated(EnumType.STRING) @Column val discountType: DiscountType,
    @Column val discountAmount: Int,
    @Column val name: String,
    @Column val sourceId: Long,
) {
    companion object {
        fun from(discountHistory: DiscountHistory): DiscountHistoryEntity {
            return DiscountHistoryEntity(
                discountHistory.id,
                discountHistory.orderId,
                discountHistory.discountType,
                discountHistory.discountAmount,
                discountHistory.name,
                discountHistory.sourceId,
            )
        }
    }
    
    fun toModel(): DiscountHistory {
        return DiscountHistory(id, orderId, discountType, discountAmount, name, sourceId)
    }
}