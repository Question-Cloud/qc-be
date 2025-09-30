package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.DiscountHistory
import com.eager.questioncloud.payment.enums.CouponType
import jakarta.persistence.*

@Entity
@Table(name = "discount_history")
class DiscountHistoryEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column @Enumerated(EnumType.STRING) var couponType: CouponType? = null,
    @Column val paymentId: Long? = null,
    @Column val orderItemId: Long? = null,
    @Column val discountAmount: Int,
    @Column val name: String,
    @Column val sourceId: Long,
) {
    companion object {
        fun from(discountHistory: DiscountHistory): DiscountHistoryEntity {
            return DiscountHistoryEntity(
                discountHistory.id,
                discountHistory.couponType,
                discountHistory.paymentId,
                discountHistory.orderItemId,
                discountHistory.discountAmount,
                discountHistory.name,
                discountHistory.sourceId,
            )
        }
    }
    
    fun toModel(): DiscountHistory {
        return DiscountHistory(id, couponType, paymentId, orderItemId, discountAmount, name, sourceId)
    }
}