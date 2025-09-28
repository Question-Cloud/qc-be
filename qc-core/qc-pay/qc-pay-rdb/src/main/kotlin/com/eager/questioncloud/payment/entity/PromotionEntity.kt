package com.eager.questioncloud.payment.entity

import com.eager.questioncloud.payment.domain.Promotion
import com.eager.questioncloud.payment.enums.PromotionType
import jakarta.persistence.*


@Entity
@Table(name = "promotion")
class PromotionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column val questionId: Long,
    @Enumerated(EnumType.STRING) @Column val promotionType: PromotionType,
    @Column val title: String,
    @Column val value: Int,
    @Column val isActive: Boolean,
) {
    companion object {
        fun from(promotion: Promotion): PromotionEntity {
            return PromotionEntity(
                promotion.id,
                promotion.questionId,
                promotion.promotionType,
                promotion.title,
                promotion.value,
                promotion.isActive
            )
        }
    }
    
    fun toModel(): Promotion {
        return Promotion(id, questionId, promotionType, title, value, isActive)
    }
}