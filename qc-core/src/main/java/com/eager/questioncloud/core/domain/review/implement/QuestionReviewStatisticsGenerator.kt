package com.eager.questioncloud.core.domain.review.implement

import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics.Companion.create
import org.springframework.stereotype.Component

@Component
class QuestionReviewStatisticsGenerator(
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository
) {
    fun generate(questionId: Long) {
        questionReviewStatisticsRepository.save(create(questionId))
    }
}
