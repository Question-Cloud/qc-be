package com.eager.questioncloud.api.review.service

import com.eager.questioncloud.api.review.dto.MyQuestionReview
import com.eager.questioncloud.api.review.event.ReviewEvent
import com.eager.questioncloud.api.review.event.ReviewEventType
import com.eager.questioncloud.api.review.implement.*
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.domain.QuestionReview
import com.eager.questioncloud.review.dto.QuestionReviewDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreReviewService(
    private val storeReviewReader: StoreReviewReader,
    private val storeReviewRegister: StoreReviewRegister,
    private val storeReviewUpdater: StoreReviewUpdater,
    private val storeReviewRemover: StoreReviewRemover,
    private val storeReviewEventProcessor: StoreReviewEventProcessor,
) {
    fun count(questionId: Long): Int {
        return storeReviewReader.count(questionId)
    }

    fun getReviewDetails(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail> {
        return storeReviewReader.getQuestionReviewDetails(questionId, userId, pagingInformation)
    }

    fun getMyReview(questionId: Long, userId: Long): MyQuestionReview {
        return storeReviewReader.getMyQuestionReview(questionId, userId)
    }

    @Transactional
    fun register(questionReview: QuestionReview) {
        storeReviewRegister.register(questionReview)
        storeReviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionReview.questionId,
                questionReview.rate,
                ReviewEventType.REGISTER
            )
        )
    }

    @Transactional
    fun modify(reviewId: Long, userId: Long, comment: String, rate: Int) {
        val (questionId, varianceRate) = storeReviewUpdater.modify(reviewId, userId, comment, rate)
        storeReviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionId,
                varianceRate,
                ReviewEventType.MODIFY
            )
        )
    }

    @Transactional
    fun delete(reviewId: Long, userId: Long) {
        val (questionId, varianceRate) = storeReviewRemover.delete(reviewId, userId)
        storeReviewEventProcessor.saveEventLog(
            ReviewEvent.create(
                questionId,
                varianceRate,
                ReviewEventType.DELETE
            )
        )
    }
}
