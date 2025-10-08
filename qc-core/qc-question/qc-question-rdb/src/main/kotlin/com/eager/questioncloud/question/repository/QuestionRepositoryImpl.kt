package com.eager.questioncloud.question.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.dto.QuestionInformation
import com.eager.questioncloud.question.entity.QPromotionEntity.promotionEntity
import com.eager.questioncloud.question.entity.QQuestionCategoryEntity
import com.eager.questioncloud.question.entity.QQuestionEntity.questionEntity
import com.eager.questioncloud.question.entity.QQuestionMetadataEntity.questionMetadataEntity
import com.eager.questioncloud.question.entity.QuestionEntity
import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.QuestionStatus
import com.eager.questioncloud.question.enums.QuestionType
import com.querydsl.core.Tuple
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Repository
class QuestionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val questionJpaRepository: QuestionJpaRepository,
) : QuestionRepository {
    private val parent = QQuestionCategoryEntity("parent")
    private val child = QQuestionCategoryEntity("child")
    
    override fun countByQuestionFilter(
        questionFilter: QuestionFilter,
    ): Int {
        return jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(
                questionLevelFilter(questionFilter.levels),
                questionCategoryFilter(questionFilter.categories),
                questionTypeFilter(questionFilter.questionType),
                questionCreatorFilter(questionFilter.creatorId),
                questionStatusFilter()
            )
            .fetchFirst() ?: 0
    }
    
    override fun getQuestionInformation(
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<QuestionInformation> {
        return questionInformationSelectFrom()
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(questionMetadataEntity)
            .on(questionMetadataEntity.questionId.eq(questionEntity.id))
            .leftJoin(promotionEntity).on(promotionEntity.questionId.eq(questionEntity.id))
            .groupBy(questionEntity.id)
            .orderBy(sort(questionFilter.sort), questionEntity.id.desc())
            .where(
                questionLevelFilter(questionFilter.levels),
                questionCategoryFilter(questionFilter.categories),
                questionTypeFilter(questionFilter.questionType),
                questionCreatorFilter(questionFilter.creatorId),
                questionStatusFilter()
            )
            .fetch()
            .stream()
            .map { tuple: Tuple -> this.parseQuestionInformationTuple(tuple) }
            .collect(Collectors.toList())
    }
    
    override fun getQuestionInformation(questionId: Long): QuestionInformation {
        val tuple = questionInformationSelectFrom()
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(questionMetadataEntity)
            .on(questionMetadataEntity.questionId.eq(questionEntity.id))
            .leftJoin(promotionEntity).on(promotionEntity.questionId.eq(questionEntity.id))
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst()
            ?: throw CoreException(Error.NOT_FOUND)
        
        return parseQuestionInformationTuple(tuple)
    }
    
    override fun getQuestionsByQuestionIds(questionIds: List<Long>): List<Question> {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.`in`(questionIds), questionStatusFilter())
            .fetch()
            .stream()
            .map { entity: QuestionEntity -> entity.toModel() }
            .collect(Collectors.toList())
    }
    
    override fun isAvailable(questionId: Long): Boolean {
        val result = jpaQueryFactory.select(questionEntity.id)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst()
        
        return result != null
    }
    
    override fun findByQuestionIdAndCreatorId(questionId: Long, creatorId: Long): Question {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(
                questionEntity.creatorId.eq(creatorId),
                questionEntity.id.eq(questionId),
                questionEntity.questionStatus.ne(QuestionStatus.Delete)
            )
            .fetchFirst()?.toModel() ?: throw CoreException(Error.NOT_FOUND)
    }
    
    override fun get(questionId: Long): Question {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.id.eq(questionId), questionStatusFilter())
            .fetchFirst()?.toModel() ?: throw CoreException(Error.UNAVAILABLE_QUESTION)
    }
    
    override fun save(question: Question): Question {
        return questionJpaRepository.save(QuestionEntity.from(question)).toModel()
    }
    
    override fun getQuestionInformationByCreatorIdWithPaging(
        creatorId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionInformation> {
        return questionInformationSelectFrom()
            .where(
                questionEntity.creatorId.eq(creatorId),
                questionEntity.questionStatus.ne(QuestionStatus.Delete)
            )
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(questionMetadataEntity)
            .on(questionMetadataEntity.questionId.eq(questionEntity.id))
            .leftJoin(promotionEntity).on(promotionEntity.questionId.eq(questionEntity.id))
            .fetch()
            .stream()
            .map { tuple -> this.parseQuestionInformationTuple(tuple) }
            .collect(Collectors.toList())
    }
    
    override fun findByCreatorId(creatorId: Long): List<Question> {
        return jpaQueryFactory.select(questionEntity)
            .from(questionEntity)
            .where(questionEntity.creatorId.eq(creatorId))
            .fetch()
            .map { it.toModel() }
    }
    
    override fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionInformation> {
        return questionInformationSelectFrom()
            .where(questionEntity.id.`in`(questionIds), questionStatusFilter())
            .leftJoin(child).on(child.id.eq(questionEntity.questionContentEntity.questionCategoryId))
            .leftJoin(parent).on(parent.id.eq(child.parentId))
            .leftJoin(questionMetadataEntity)
            .on(questionMetadataEntity.questionId.eq(questionEntity.id))
            .leftJoin(promotionEntity).on(promotionEntity.questionId.eq(questionEntity.id))
            .fetch()
            .stream()
            .map { tuple -> this.parseQuestionInformationTuple(tuple) }
            .collect(Collectors.toList())
    }
    
    override fun countByCreatorId(creatorId: Long): Int {
        return jpaQueryFactory.select(questionEntity.id.count().intValue())
            .from(questionEntity)
            .where(
                questionEntity.creatorId.eq(creatorId),
                questionEntity.questionStatus.ne(QuestionStatus.Delete)
            )
            .fetchFirst() ?: 0
    }
    
    @Transactional
    override fun increaseQuestionCount(questionId: Long) {
        jpaQueryFactory.update(questionEntity)
            .set(questionEntity.count, questionEntity.count.add(1))
            .where(questionEntity.id.eq(questionId))
            .execute()
    }
    
    override fun deleteAllInBatch() {
        questionJpaRepository.deleteAllInBatch()
    }
    
    private fun sort(sort: QuestionSortType): OrderSpecifier<*> {
        return when (sort) {
            QuestionSortType.Popularity -> {
                questionEntity.count.desc()
            }
            
            QuestionSortType.Rate -> {
                questionMetadataEntity.reviewAverageRate.desc()
            }
            
            QuestionSortType.Latest -> {
                questionEntity.createdAt.desc()
            }
            
            QuestionSortType.LEVEL -> {
                questionEntity.questionContentEntity.questionLevel.desc()
            }
        }
    }
    
    private fun questionLevelFilter(levels: List<QuestionLevel?>?): BooleanExpression? {
        if (levels.isNullOrEmpty()) {
            return null
        }
        return questionEntity.questionContentEntity.questionLevel.`in`(levels)
    }
    
    private fun questionCategoryFilter(categories: List<Long?>?): BooleanExpression? {
        if (categories.isNullOrEmpty()) {
            return null
        }
        return questionEntity.questionContentEntity.questionCategoryId.`in`(categories)
    }
    
    private fun questionTypeFilter(questionType: QuestionType?): BooleanExpression? {
        return questionType?.let {
            questionEntity.questionContentEntity.questionType.eq(questionType)
        }
    }
    
    private fun questionCreatorFilter(creatorId: Long?): BooleanExpression? {
        return creatorId?.let {
            questionEntity.creatorId.eq(creatorId)
        }
    }
    
    private fun questionStatusFilter(): BooleanExpression {
        return questionEntity.questionStatus.ne(QuestionStatus.Delete)
            .and(questionEntity.questionStatus.ne(QuestionStatus.UnAvailable))
    }
    
    private fun parseQuestionInformationTuple(tuple: Tuple): QuestionInformation {
        return QuestionInformation(
            tuple.get(questionEntity.id)!!,
            tuple.get(questionEntity.creatorId)!!,
            tuple.get(questionEntity.questionContentEntity.title)!!,
            tuple.get(questionEntity.questionContentEntity.subject)!!,
            tuple.get(parent.title)!!,
            tuple.get(child.title)!!,
            tuple.get(questionEntity.questionContentEntity.thumbnail)!!,
            tuple.get(questionEntity.questionContentEntity.questionLevel)!!,
            tuple.get(questionEntity.questionContentEntity.price)!!,
            tuple.get(promotionEntity.title),
            tuple.get(promotionEntity.salePrice),
            tuple.get(questionMetadataEntity.reviewAverageRate)!!,
        )
    }
    
    private fun questionInformationSelectFrom(): JPAQuery<Tuple> {
        return jpaQueryFactory.query()
            .select(
                questionEntity.id,
                questionEntity.questionContentEntity.title,
                questionEntity.creatorId,
                questionEntity.count,
                parent.title,
                child.title,
                questionEntity.questionContentEntity.thumbnail,
                questionEntity.questionContentEntity.questionLevel,
                questionEntity.questionContentEntity.price,
                promotionEntity.title,
                promotionEntity.salePrice,
                questionMetadataEntity.reviewAverageRate,
                questionEntity.questionContentEntity.subject
            )
            .from(questionEntity)
    }
}
