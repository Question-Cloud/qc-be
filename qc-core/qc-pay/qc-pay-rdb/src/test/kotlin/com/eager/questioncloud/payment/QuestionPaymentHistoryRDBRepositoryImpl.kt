package com.eager.questioncloud.payment

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.QQuestionPaymentHistoryRDBEntity.questionPaymentHistoryRDBEntity
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionPaymentHistoryRDBRepositoryImpl(
    private val questionPaymentHistoryRDBJpaRepository: QuestionPaymentHistoryRDBJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : QuestionPaymentHistoryRDBRepository {
    override fun save(questionPaymentHistory: QuestionPaymentHistory) {
        questionPaymentHistoryRDBJpaRepository.save(QuestionPaymentHistoryRDBEntity.createNewEntity(questionPaymentHistory))
    }
    
    override fun getQuestionPaymentHistory(userId: Long, pagingInformation: PagingInformation): List<QuestionPaymentHistory> {
        return jpaQueryFactory.select(questionPaymentHistoryRDBEntity)
            .from(questionPaymentHistoryRDBEntity)
            .where(questionPaymentHistoryRDBEntity.userId.eq(userId))
            .orderBy(questionPaymentHistoryRDBEntity.orderId.desc())
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .map {
                it.history
            }
    }
}