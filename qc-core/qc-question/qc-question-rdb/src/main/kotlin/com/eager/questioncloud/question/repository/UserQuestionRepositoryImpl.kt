package com.eager.questioncloud.question.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.UserQuestion
import com.eager.questioncloud.question.dto.UserQuestionContent
import com.eager.questioncloud.question.entity.QQuestionCategoryEntity
import com.eager.questioncloud.question.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.question.entity.QUserQuestionEntity.userQuestionEntity
import com.eager.questioncloud.question.entity.UserQuestionEntity
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
        return UserQuestionEntity.toModel(
            userQuestionJpaRepository.saveAll(
                UserQuestionEntity.from(
                    userQuestionLibraries
                )
            )
        )
    }
    
    override fun isOwned(userId: Long, questionIds: List<Long>): Boolean {
        return userQuestionJpaRepository.existsByUserIdAndQuestionIdIn(userId, questionIds)
    }
    
    override fun isOwned(userId: Long, questionId: Long): Boolean {
        return userQuestionJpaRepository.existsByUserIdAndQuestionId(userId, questionId)
    }
    
    override fun getUserQuestions(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<UserQuestionContent> {
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
            .where(userQuestionEntity.userId.eq(userId))
            .innerJoin(questionEntity).on(questionEntityJoinCondition(questionFilter))
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
    }
    
    override fun countUserQuestions(userId: Long, questionFilter: QuestionFilter): Int {
        val parent = QQuestionCategoryEntity("parent")
        val child = QQuestionCategoryEntity("child")
        return jpaQueryFactory.select(userQuestionEntity.id.count().intValue())
            .from(userQuestionEntity)
            .where(userQuestionEntity.userId.eq(userId))
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
        
        if (questionFilter.creatorId != null) {
            builder.and(questionEntity.creatorId.eq(questionFilter.creatorId))
        }
        
        builder.and(questionEntity.id.eq(userQuestionEntity.questionId))
        
        return builder
    }
}
