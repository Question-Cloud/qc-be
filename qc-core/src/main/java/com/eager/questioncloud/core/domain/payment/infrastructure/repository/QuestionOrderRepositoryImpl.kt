package com.eager.questioncloud.core.domain.payment.infrastructure.repository

import com.eager.questioncloud.core.domain.payment.infrastructure.entity.QuestionOrderEntity.Companion.from
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder
import org.springframework.stereotype.Repository

@Repository
class QuestionOrderRepositoryImpl(
    private val questionOrderJpaRepository: QuestionOrderJpaRepository
) : QuestionOrderRepository {

    override fun save(questionOrder: QuestionOrder) {
        questionOrderJpaRepository.saveAll(from(questionOrder))
    }

    override fun deleteAllInBatch() {
        questionOrderJpaRepository.deleteAllInBatch()
    }
}
