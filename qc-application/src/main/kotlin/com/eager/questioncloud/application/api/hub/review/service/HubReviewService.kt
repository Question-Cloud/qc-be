package com.eager.questioncloud.application.api.hub.review.service

import com.eager.questioncloud.application.api.hub.review.dto.MyQuestionReview
import com.eager.questioncloud.application.api.hub.review.event.ReviewEvent
import com.eager.questioncloud.application.api.hub.review.event.ReviewEventType
import com.eager.questioncloud.application.api.hub.review.implement.*
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HubReviewService(
    private val hubReviewReader: HubReviewReader,
    private val hubReviewRegister: HubReviewRegister,
    private val hubReviewUpdater: HubReviewUpdater,
    private val hubReviewRemover: HubReviewRemover,
    private val reviewEventProcessor: ReviewEventProcessor,
) {
    fun count(questionId: Long): Int {
        return hubReviewReader.count(questionId)
    }

    fun getQuestionReviewDetails(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail> {
        return hubReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
    }

    fun getMyQuestionReview(questionId: Long, userId: Long): MyQuestionReview {
        return hubReviewReader.getMyQuestionReview(questionId, userId)
    }

    @Transactional
    fun register(questionReview: QuestionReview) {
        hubReviewRegister.register(questionReview)
        reviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionReview.questionId,
                questionReview.rate,
                ReviewEventType.REGISTER
            )
        )
    }

    @Transactional
    fun modify(reviewId: Long, userId: Long, comment: String, rate: Int) {
        val (questionId, varianceRate) = hubReviewUpdater.modify(reviewId, userId, comment, rate)
        reviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionId,
                varianceRate,
                ReviewEventType.MODIFY
            )
        )
    }

    @Transactional
    fun delete(reviewId: Long, userId: Long) {
        val (questionId, varianceRate) = hubReviewRemover.delete(reviewId, userId)
        reviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionId,
                varianceRate,
                ReviewEventType.DELETE
            )
        )
    }
}
