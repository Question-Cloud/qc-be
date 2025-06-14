package com.eager.questioncloud.review.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.dto.ReviewerStatistics

interface QuestionReviewRepository {
    fun countByQuestionId(questionId: Long): Int

    fun findByQuestionIdWithPagination(questionId: Long, pagingInformation: PagingInformation): List<QuestionReview>

    fun findByQuestionIdAndUserId(questionId: Long, userId: Long): QuestionReview

    fun findByIdAndUserId(reviewId: Long, userId: Long): QuestionReview

    fun isWritten(userId: Long, questionId: Long): Boolean

    fun save(questionReview: QuestionReview): QuestionReview

    fun getReviewerStatistics(userIds: List<Long>): Map<Long, ReviewerStatistics>
}
