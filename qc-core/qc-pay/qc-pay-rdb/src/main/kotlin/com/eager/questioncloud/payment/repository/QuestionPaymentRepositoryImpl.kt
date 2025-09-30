package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.dto.QuestionPaymentData
import com.eager.questioncloud.payment.entity.QQuestionPaymentEntity.questionPaymentEntity
import com.eager.questioncloud.payment.entity.QuestionPaymentEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionPaymentRepositoryImpl(
    private val questionPaymentJpaRepository: QuestionPaymentJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory
) : QuestionPaymentRepository {
    
    override fun save(questionPayment: QuestionPayment): QuestionPayment {
        val questionPaymentEntity = questionPaymentJpaRepository.save(
            QuestionPaymentEntity.createNewEntity(
                questionPayment
            )
        )
        questionPayment.stored(questionPaymentEntity.paymentId)
        return questionPayment
    }
    
    override fun countByUserId(userId: Long): Int {
        return questionPaymentJpaRepository.countByUserId(userId)
    }
    
    override fun getQuestionPaymentData(paymentId: Long): QuestionPaymentData {
        val result = jpaQueryFactory.select(
            Projections.constructor(
                QuestionPaymentData::class.java, questionPaymentEntity.orderId,
                questionPaymentEntity.userId, questionPaymentEntity.originalAmount, questionPaymentEntity.realAmount
            )
        )
            .from(questionPaymentEntity)
            .where(questionPaymentEntity.paymentId.eq(paymentId))
            .fetchFirst()
        
        if (result == null) {
            throw CoreException(Error.NOT_FOUND)
        }
        
        return result
    }
}
