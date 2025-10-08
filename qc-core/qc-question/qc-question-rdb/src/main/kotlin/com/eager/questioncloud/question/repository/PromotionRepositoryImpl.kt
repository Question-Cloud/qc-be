package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.domain.Promotion
import com.eager.questioncloud.question.entity.PromotionEntity
import com.eager.questioncloud.question.entity.QPromotionEntity.promotionEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class PromotionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val promotionJpaRepository: PromotionJpaRepository
) : PromotionRepository {
    override fun save(promotion: Promotion): Promotion {
        return promotionJpaRepository.save(PromotionEntity.from(promotion)).toModel()
    }
    
    override fun findByQuestionIdIn(questionIds: List<Long>): List<Promotion> {
        return jpaQueryFactory.select(promotionEntity)
            .from(promotionEntity)
            .where(promotionEntity.questionId.`in`(questionIds), promotionEntity.isActive.isTrue())
            .fetch()
            .map { it.toModel() }
    }
}