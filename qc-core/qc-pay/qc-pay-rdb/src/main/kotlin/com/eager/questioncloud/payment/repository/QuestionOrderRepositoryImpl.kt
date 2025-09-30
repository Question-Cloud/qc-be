package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
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
        val orderItemEntities = questionOrderJpaRepository.saveAll(QuestionOrderEntity.from(questionOrder))
        questionOrder.items.forEach {
            val savedEntity = orderItemEntities.find { entity -> entity.questionId == it.questionId }
            
            if (savedEntity == null) {
                throw CoreException(Error.PAYMENT_ERROR)
            }
            
            it.stored(savedEntity.id)
        }
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
