package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionPayment
import com.eager.questioncloud.payment.entity.QuestionPaymentEntity
import org.springframework.stereotype.Repository

@Repository
class QuestionPaymentRepositoryImpl(
    private val questionPaymentJpaRepository: QuestionPaymentJpaRepository,
) : QuestionPaymentRepository {
    
    override fun save(questionPayment: QuestionPayment): QuestionPayment {
        questionPaymentJpaRepository.save(
            QuestionPaymentEntity.createNewEntity(
                questionPayment
            )
        )
        return questionPayment
    }
    
    override fun countByUserId(userId: Long): Int {
        return questionPaymentJpaRepository.countByUserId(userId)
    }
}
