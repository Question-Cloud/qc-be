package com.eager.questioncloud.review.infrastructure.repository

import com.eager.questioncloud.common.exception.CoreException
import com.eager.questioncloud.common.exception.Error
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.dto.ReviewerStatistics
import com.eager.questioncloud.review.infrastructure.entity.QQuestionReviewEntity.questionReviewEntity
import com.eager.questioncloud.review.infrastructure.entity.QuestionReviewEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class QuestionReviewRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val questionReviewJpaRepository: QuestionReviewJpaRepository,
) : QuestionReviewRepository {

    override fun countByQuestionId(questionId: Long): Int {
        return jpaQueryFactory.select(questionReviewEntity.id.count().intValue())
            .from(questionReviewEntity)
            .where(
                questionReviewEntity.questionId.eq(questionId),
                questionReviewEntity.isDeleted.isFalse()
            )
            .fetchFirst() ?: 0
    }

    override fun findByQuestionIdWithPagination(
        questionId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReview> {
        return jpaQueryFactory.select(questionReviewEntity)
            .from(questionReviewEntity)
            .where(questionReviewEntity.questionId.eq(questionId), questionReviewEntity.isDeleted.isFalse())
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .map { it.toModel() }
    }

    override fun findByQuestionIdAndUserId(questionId: Long, userId: Long): QuestionReview {
        return questionReviewJpaRepository.findByQuestionIdAndReviewerIdAndIsDeletedFalse(questionId, userId)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun findByIdAndUserId(reviewId: Long, userId: Long): QuestionReview {
        return jpaQueryFactory.select(questionReviewEntity)
            .from(questionReviewEntity)
            .where(
                questionReviewEntity.id.eq(reviewId),
                questionReviewEntity.reviewerId.eq(userId),
                questionReviewEntity.isDeleted.isFalse()
            )
            .fetchFirst()?.toModel() ?: throw CoreException(Error.NOT_FOUND)
    }

    override fun isWritten(userId: Long, questionId: Long): Boolean {
        val reviewId = jpaQueryFactory.select(questionReviewEntity.id)
            .from(questionReviewEntity)
            .where(
                questionReviewEntity.questionId.eq(questionId),
                questionReviewEntity.reviewerId.eq(userId),
                questionReviewEntity.isDeleted.isFalse()
            )
            .fetchFirst()

        return reviewId != null
    }

    override fun save(questionReview: QuestionReview): QuestionReview {
        return questionReviewJpaRepository.save(QuestionReviewEntity.from(questionReview)).toModel()
    }

    override fun getReviewerStatistics(userIds: List<Long>): Map<Long, ReviewerStatistics> {
        return jpaQueryFactory.select(
            questionReviewEntity.reviewerId, questionReviewEntity.rate.avg(),
            questionReviewEntity.id.count().intValue()
        )
            .from(questionReviewEntity)
            .where(questionReviewEntity.reviewerId.`in`(userIds))
            .groupBy(questionReviewEntity.reviewerId)
            .fetch()
            .associate { tuple ->
                tuple.get(questionReviewEntity.reviewerId)!! to
                        ReviewerStatistics(
                            tuple.get(questionReviewEntity.id.count().intValue())!!,
                            tuple.get(questionReviewEntity.rate.avg())!!
                        )
            }
    }
}
