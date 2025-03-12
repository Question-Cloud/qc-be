package com.eager.questioncloud.application.api.hub.review.service

import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.application.api.hub.review.implement.HubReviewRegister
import com.eager.questioncloud.application.api.hub.review.implement.HubReviewRemover
import com.eager.questioncloud.application.api.hub.review.implement.HubReviewUpdater
import com.eager.questioncloud.application.api.hub.review.implement.ReviewEventProcessor
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview.Companion.from
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HubReviewService(
    private val questionReviewRepository: QuestionReviewRepository,
    private val hubReviewRegister: HubReviewRegister,
    private val hubReviewUpdater: HubReviewUpdater,
    private val hubReviewRemover: HubReviewRemover,
    private val reviewEventProcessor: ReviewEventProcessor,
) {
    fun getTotal(questionId: Long): Int {
        return questionReviewRepository.getTotal(questionId)
    }

    fun getQuestionReviews(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail> {
        return questionReviewRepository.getQuestionReviews(questionId, userId, pagingInformation)
    }

    fun getMyQuestionReview(questionId: Long, userId: Long): MyQuestionReview {
        val questionReview = questionReviewRepository.getMyQuestionReview(questionId, userId)
        return from(questionReview)
    }

    @Transactional
    fun register(questionReview: QuestionReview) {
        hubReviewRegister.register(questionReview)

        reviewEventProcessor.createEvent(
            questionReview.questionId,
            questionReview.rate,
            ReviewEventType.REGISTER
        )
    }

    @Transactional
    fun modify(reviewId: Long, userId: Long, comment: String, rate: Int) {
        val (questionId, varianceRate) = hubReviewUpdater.modify(reviewId, userId, comment, rate)

        reviewEventProcessor.createEvent(questionId, varianceRate, ReviewEventType.MODIFY)
    }

    @Transactional
    fun delete(reviewId: Long, userId: Long) {
        val (questionId, varianceRate) = hubReviewRemover.delete(reviewId, userId)

        reviewEventProcessor.createEvent(questionId, varianceRate, ReviewEventType.DELETE)
    }
}
