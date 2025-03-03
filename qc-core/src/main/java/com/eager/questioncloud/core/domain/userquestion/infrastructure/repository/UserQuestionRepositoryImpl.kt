package com.eager.questioncloud.core.domain.userquestion.infrastructure.repository

import com.eager.questioncloud.core.domain.creator.infrastructure.entity.QCreatorEntity.creatorEntity
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionCategoryEntity
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail
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

    override fun getUserQuestions(questionFilter: QuestionFilter): List<UserQuestionDetail> {
        val parent = QQuestionCategoryEntity("parent")
        val child = QQuestionCategoryEntity("child")
        return jpaQueryFactory.select(
            Projections.constructor(
                UserQuestionDetail::class.java,
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                userEntity.userInformationEntity.name,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.fileUrl,
                questionEntity.questionContentEntity.explanationUrl
            )
        )
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(questionFilter.userId))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
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
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .innerJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .innerJoin(parent).on(parent.id.eq(child.parentId))
            .innerJoin(creatorEntity).on(creatorEntity.id.eq(questionEntity.creatorId))
            .innerJoin(userEntity).on(userEntity.uid.eq(creatorEntity.userId))
            .fetchFirst() ?: 0
    }

    override fun deleteAllInBatch() {
        userQuestionJpaRepository.deleteAllInBatch()
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
