package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionPaymentEntity.Companion.from
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment
import org.springframework.stereotype.Repository

@Repository
class QuestionPaymentRepositoryImpl(
    private val questionPaymentJpaRepository: QuestionPaymentJpaRepository
) : QuestionPaymentRepository {

    override fun save(questionPayment: QuestionPayment): QuestionPayment {
        val entity = questionPaymentJpaRepository.save(from(questionPayment))
        return questionPayment
    }

    override fun countByUserId(userId: Long): Int {
        return questionPaymentJpaRepository.countByUserId(userId)
    }
}
