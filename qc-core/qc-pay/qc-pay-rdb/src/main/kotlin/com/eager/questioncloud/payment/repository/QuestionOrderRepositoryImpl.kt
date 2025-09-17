package com.eager.questioncloud.payment.repository

import com.eager.questioncloud.payment.domain.QuestionOrder
import com.eager.questioncloud.payment.entity.QuestionOrderEntity
import org.springframework.stereotype.Repository

@Repository
class QuestionOrderRepositoryImpl(
    private val questionOrderJpaRepository: QuestionOrderJpaRepository
) : QuestionOrderRepository {
    
    override fun save(questionOrder: QuestionOrder) {
        questionOrderJpaRepository.saveAll(QuestionOrderEntity.from(questionOrder))
    }
    
    override fun deleteAllInBatch() {
        questionOrderJpaRepository.deleteAllInBatch()
    }
}
