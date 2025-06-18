package com.eager.questioncloud.payment.infrastructure.repository

import com.eager.questioncloud.payment.domain.QuestionPaymentEventLog
import com.eager.questioncloud.payment.infrastructure.entity.QQuestionPaymentEventLogEntity.questionPaymentEventLogEntity
import com.eager.questioncloud.payment.infrastructure.entity.QuestionPaymentEventLogEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class QuestionPaymentEventLogRepositoryImpl(
    private val questionPaymentEventLogJpaRepository: QuestionPaymentEventLogJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : QuestionPaymentEventLogRepository {
    override fun save(questionPaymentEventLog: QuestionPaymentEventLog) {
        questionPaymentEventLogJpaRepository.save(QuestionPaymentEventLogEntity.createNewEntity(questionPaymentEventLog))
    }

    override fun getUnPublishedEvent(): List<QuestionPaymentEventLog> {
        return jpaQueryFactory.select(questionPaymentEventLogEntity)
            .from(questionPaymentEventLogEntity)
            .where(questionPaymentEventLogEntity.isPublish.isFalse)
            .orderBy(questionPaymentEventLogEntity.eventId.asc())
            .limit(1000)
            .fetch()
            .stream()
            .map { entity -> entity.toModel() }
            .toList()
    }

    override fun findByEventId(eventId: String): QuestionPaymentEventLog {
        return questionPaymentEventLogJpaRepository.findById(eventId)
            .orElseThrow { RuntimeException("Not Found QuestionPayment Event Log : $eventId") }
            .toModel()
    }

    @Transactional
    override fun publish(eventId: String) {
        jpaQueryFactory.update(questionPaymentEventLogEntity)
            .set(questionPaymentEventLogEntity.isPublish, true)
            .where(questionPaymentEventLogEntity.eventId.eq(eventId))
            .execute()
    }

    @Transactional
    override fun publish(eventIds: List<String>) {
        jpaQueryFactory.update(questionPaymentEventLogEntity)
            .set(questionPaymentEventLogEntity.isPublish, true)
            .where(questionPaymentEventLogEntity.eventId.`in`(eventIds))
            .execute()
    }
}