package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewEventLogEntity.questionReviewEventLogEntity
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewEventLogEntity
import com.eager.questioncloud.core.domain.review.model.QuestionReviewEventLog
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class QuestionReviewEventLogRepositoryImpl(
    private val questionReviewEventLogJpaRepository: QuestionReviewEventLogJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : QuestionReviewEventLogRepository {

    override fun findByEventId(eventId: String): QuestionReviewEventLog {
        return questionReviewEventLogJpaRepository.findById(eventId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getUnPublishedEvent(): List<QuestionReviewEventLog> {
        return jpaQueryFactory.select(questionReviewEventLogEntity)
            .from(questionReviewEventLogEntity)
            .where(questionReviewEventLogEntity.isPublish.isFalse)
            .orderBy(questionReviewEventLogEntity.eventId.asc())
            .limit(1000)
            .fetch()
            .stream()
            .map { entity -> entity.toModel() }
            .toList()
    }

    override fun save(questionReviewEventLog: QuestionReviewEventLog) {
        questionReviewEventLogJpaRepository.save(QuestionReviewEventLogEntity.createNewEntity(questionReviewEventLog))
    }

    @Transactional
    override fun publish(eventId: String) {
        jpaQueryFactory.update(questionReviewEventLogEntity)
            .where(questionReviewEventLogEntity.eventId.eq(eventId))
            .set(questionReviewEventLogEntity.isPublish, true)
            .execute()
    }

    @Transactional
    override fun publish(eventIds: List<String>) {
        jpaQueryFactory.update(questionReviewEventLogEntity)
            .set(questionReviewEventLogEntity.isPublish, true)
            .where(questionReviewEventLogEntity.eventId.`in`(eventIds))
            .execute()
    }
}