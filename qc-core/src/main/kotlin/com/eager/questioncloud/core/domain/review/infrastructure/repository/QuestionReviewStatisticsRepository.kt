package com.eager.questioncloud.core.domain.review.infrastructure.repository

import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics

interface QuestionReviewStatisticsRepository {
    fun get(questionId: Long): QuestionReviewStatistics

    fun getForUpdate(questionId: Long): QuestionReviewStatistics

    fun save(questionReviewStatistics: QuestionReviewStatistics): QuestionReviewStatistics

    fun update(questionReviewStatistics: QuestionReviewStatistics)

    fun deleteAllInBatch()
}
