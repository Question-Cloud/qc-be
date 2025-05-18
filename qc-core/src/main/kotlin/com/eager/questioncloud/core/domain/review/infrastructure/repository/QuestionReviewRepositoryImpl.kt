package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewEntity
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QQuestionReviewEntity.questionReviewEntity
import com.eager.questioncloud.core.domain.review.infrastructure.entity.QuestionReviewEntity.Companion.from
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import com.eager.questioncloud.core.domain.user.enums.UserType
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.core.types.dsl.MathExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class QuestionReviewRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val questionReviewJpaRepository: QuestionReviewJpaRepository,
) : QuestionReviewRepository {

    override fun getTotal(questionId: Long): Int {
        return jpaQueryFactory.select(questionReviewEntity.id.count().intValue())
            .from(questionReviewEntity)
            .where(
                questionReviewEntity.questionId.eq(questionId),
                questionReviewEntity.isDeleted.isFalse()
            )
            .fetchFirst() ?: 0
    }

    override fun getQuestionReviews(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail> {
        val profile = QQuestionReviewEntity("profile")

        return jpaQueryFactory.select(
            questionReviewEntity.id,
            userEntity.userInformationEntity.name,
            userEntity.uid,
            userEntity.userType,
            profile.id.count().intValue(),
            MathExpressions.round(profile.rate.avg(), 1),
            questionReviewEntity.rate,
            questionReviewEntity.comment,
            questionReviewEntity.createdAt
        )
            .from(questionReviewEntity)
            .where(questionReviewEntity.questionId.eq(questionId), questionReviewEntity.isDeleted.isFalse())
            .leftJoin(profile).on(profile.reviewerId.eq(questionReviewEntity.reviewerId), profile.isDeleted.isFalse())
            .leftJoin(userEntity)
            .on(userEntity.uid.eq(questionReviewEntity.reviewerId))
            .groupBy(questionReviewEntity.id)
            .offset(pagingInformation.offset.toLong())
            .limit(pagingInformation.size.toLong())
            .fetch()
            .stream()
            .map { tuple ->
                QuestionReviewDetail(
                    tuple.get(questionReviewEntity.id)!!,
                    tuple.get(userEntity.userInformationEntity.name)!!,
                    UserType.CreatorUser == tuple.get(userEntity.userType),
                    userId == tuple.get(userEntity.uid),
                    tuple.get(profile.id.count().intValue())!!,
                    tuple.get(MathExpressions.round(profile.rate.avg(), 1))!!,
                    tuple.get(questionReviewEntity.rate)!!,
                    tuple.get(questionReviewEntity.comment)!!,
                    tuple.get(questionReviewEntity.createdAt)!!
                )
            }
            .collect(Collectors.toList())
    }

    override fun getMyQuestionReview(questionId: Long, userId: Long): QuestionReview {
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
        return questionReviewJpaRepository.save(from(questionReview)).toModel()
    }
}
