package com.eager.questioncloud.question.repository

import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.domain.QuestionContent
import com.eager.questioncloud.question.entity.QQuestionEntity.questionEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ProjectionsReadPerformanceMock(
    private val jpaQueryFactory: JPAQueryFactory,
    private val questionJpaRepository: QuestionJpaRepository,
) {
    fun jpaSelect(): List<Question> {
        return questionJpaRepository.findAll().map { it.toModel() }
    }
    
    fun simpleSelect(): List<Question> {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .fetch()
            .map { it.toModel() }
    }
    
    fun projectionsSelect(): List<Question> {
        return jpaQueryFactory.select(
            Projections.constructor(
                Question::class.java,
                questionEntity.id,
                questionEntity.creatorId,
                Projections.constructor(
                    QuestionContent::class.java,
                    questionEntity.questionContentEntity.questionCategoryId,
                    questionEntity.questionContentEntity.subject,
                    questionEntity.questionContentEntity.title,
                    questionEntity.questionContentEntity.description,
                    questionEntity.questionContentEntity.thumbnail,
                    questionEntity.questionContentEntity.fileUrl,
                    questionEntity.questionContentEntity.explanationUrl,
                    questionEntity.questionContentEntity.questionType,
                    questionEntity.questionContentEntity.questionLevel,
                    questionEntity.questionContentEntity.price
                ),
                questionEntity.questionStatus,
                questionEntity.count,
                questionEntity.createdAt
            )
        )
            .from(questionEntity)
            .fetch()
    }
}