package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.dto.QuestionOrderData
import com.eager.questioncloud.payment.entity.QQuestionOrderEntity.questionOrderEntity
import com.eager.questioncloud.payment.entity.QuestionOrderEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionOrderRepositoryImpl(
    private val questionOrderJpaRepository: QuestionOrderJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : QuestionOrderRepository {
    
    override fun save(questionOrder: QuestionOrder) {
        questionOrderJpaRepository.saveAll(QuestionOrderEntity.from(questionOrder))
    }
    
    override fun deleteAllInBatch() {
        questionOrderJpaRepository.deleteAllInBatch()
    }
    
    override fun getQuestionOrderData(orderId: String): List<QuestionOrderData> {
        return jpaQueryFactory.select(
            Projections.constructor(
                QuestionOrderData::class.java,
                questionOrderEntity.id,
                questionOrderEntity.orderId,
                questionOrderEntity.questionId,
                questionOrderEntity.originalPrice,
                questionOrderEntity.realPrice,
                questionOrderEntity.promotionId,
                questionOrderEntity.promotionName,
                questionOrderEntity.promotionDiscountAmount
            )
        )
            .from(questionOrderEntity)
            .where(questionOrderEntity.orderId.eq(orderId))
            .fetch()
    }
}
