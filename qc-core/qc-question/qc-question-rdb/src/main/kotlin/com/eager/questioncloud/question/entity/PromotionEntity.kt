package com.eager.questioncloud.question.entity

import com.eager.questioncloud.question.domain.Promotion
import jakarta.persistence.*


@Entity
@Table(name = "promotion")
class PromotionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long = 0,
    @Column val questionId: Long,
    @Column val title: String,
    @Column val salePrice: Int,
    @Column val isActive: Boolean,
) {
    companion object {
        fun from(promotion: Promotion): PromotionEntity {
            return PromotionEntity(
                promotion.id,
                promotion.questionId,
                promotion.title,
                promotion.salePrice,
                promotion.isActive
            )
        }
    }
    
    fun toModel(): Promotion {
        return Promotion(id, questionId, title, salePrice, isActive)
    }
}