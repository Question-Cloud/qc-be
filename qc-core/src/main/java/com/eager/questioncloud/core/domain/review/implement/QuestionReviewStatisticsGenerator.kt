package com.eager.questioncloud.core.domain.review.implement

import com.eager.questioncloud.core.domain.question.event.RegisteredQuestionEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewStatisticsRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReviewStatistics.Companion.create
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class QuestionReviewStatisticsGenerator(
    private val questionReviewStatisticsRepository: QuestionReviewStatisticsRepository
) {

    @EventListener
    fun generate(event: RegisteredQuestionEvent) {
        questionReviewStatisticsRepository.save(create(event.question.id!!))
    }
}
