package com.eager.questioncloud.core.domain.userquestion.infrastructure.repository

import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionContent
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.QUserQuestionEntity.userQuestionEntity
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity.Companion.from
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity.Companion.toModel
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserQuestionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val userQuestionJpaRepository: UserQuestionJpaRepository,
) : UserQuestionRepository {
    override fun saveAll(userQuestionLibraries: List<UserQuestion>): List<UserQuestion> {
        return toModel(userQuestionJpaRepository.saveAll(from(userQuestionLibraries)))
    }

    override fun isOwned(userId: Long, questionIds: List<Long>): Boolean {
        return userQuestionJpaRepository.existsByUserIdAndQuestionIdIn(userId, questionIds)
    }

    override fun isOwned(userId: Long, questionId: Long): Boolean {
        return userQuestionJpaRepository.existsByUserIdAndQuestionId(userId, questionId)
    }

    override fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionContent> {
        val parent = QQuestionCategoryEntity("parent")
        val child = QQuestionCategoryEntity("child")
        return jpaQueryFactory.select(
            Projections.constructor(
                UserQuestionContent::class.java,
                questionEntity.id,
                questionEntity.creatorId,
                questionEntity.questionContentEntity.title,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.fileUrl,
                questionEntity.questionContentEntity.explanationUrl
            )
        )
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(questionFilter.userId))
            .leftJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .offset(questionFilter.pagingInformation.offset.toLong())
            .limit(questionFilter.pagingInformation.size.toLong())
            .fetch()
    }

    override fun countUserQuestions(questionFilter: QuestionFilter): Int {
        val parent = QQuestionCategoryEntity("parent")
        val child = QQuestionCategoryEntity("child")
        return jpaQueryFactory.select(userQuestionEntity.id.count().intValue())
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(questionFilter.userId))
            .leftJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .fetchFirst() ?: 0
    }

    override fun deleteAllInBatch() {
        userQuestionJpaRepository.deleteAllInBatch()
    }

    override fun findByQuestionIdInAndUserId(questionIds: List<Long>, userId: Long): List<UserQuestion> {
        return jpaQueryFactory.select(userQuestionEntity)
            .from(userQuestionEntity)
            .where(userQuestionEntity.questionId.`in`(questionIds), userQuestionEntity.userId.eq(userId))
            .fetch()
            .map { it.toModel() }
    }

    private fun questionEntityJoinCondition(questionFilter: QuestionFilter): BooleanBuilder {
        val builder = BooleanBuilder()
        if (!questionFilter.levels.isNullOrEmpty()) {
            builder.and(questionEntity.questionContentEntity.questionLevel.`in`(questionFilter.levels))
        }

        if (!questionFilter.categories.isNullOrEmpty()) {
            builder.and(questionEntity.questionContentEntity.questionCategoryId.`in`(questionFilter.categories))
        }

        if (questionFilter.questionType != null) {
            builder.and(questionEntity.questionContentEntity.questionType.eq(questionFilter.questionType))
        }

        builder.and(questionEntity.id.eq(userQuestionEntity.questionId))

        return builder
    }
}
