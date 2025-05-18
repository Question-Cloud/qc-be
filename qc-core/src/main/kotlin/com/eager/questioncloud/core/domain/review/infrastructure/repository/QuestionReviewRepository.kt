package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.model.QuestionReview

interface QuestionReviewRepository {
    fun getTotal(questionId: Long): Int

    fun getQuestionReviews(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail>

    fun getMyQuestionReview(questionId: Long, userId: Long): QuestionReview

    fun findByIdAndUserId(reviewId: Long, userId: Long): QuestionReview

    fun isWritten(userId: Long, questionId: Long): Boolean

    fun save(questionReview: QuestionReview): QuestionReview
}
