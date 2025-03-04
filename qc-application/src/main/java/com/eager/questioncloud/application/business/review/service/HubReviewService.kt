package com.eager.questioncloud.application.business.review.service

import com.eager.questioncloud.application.business.review.implement.HubReviewRegister
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview.Companion.from
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.event.DeletedReviewEvent
import com.eager.questioncloud.core.domain.review.event.ModifiedReviewEvent
import com.eager.questioncloud.core.domain.review.event.RegisteredReviewEvent
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.core.domain.review.model.QuestionReview
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class HubReviewService(
    private val questionReviewRepository: QuestionReviewRepository,
    private val hubReviewRegister: HubReviewRegister,
    private val applicationEventPublisher: ApplicationEventPublisher,
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

    fun register(questionReview: QuestionReview) {
        hubReviewRegister.register(questionReview)
        applicationEventPublisher.publishEvent(
            RegisteredReviewEvent.create(questionReview.questionId, questionReview.rate)
        )
    }

    fun modify(reviewId: Long, userId: Long, comment: String, rate: Int) {
        val questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId)
        val varianceRate = questionReview.modify(comment, rate)
        questionReviewRepository.save(questionReview)
        applicationEventPublisher.publishEvent(ModifiedReviewEvent.create(questionReview.questionId, varianceRate))
    }

    fun delete(reviewId: Long, userId: Long) {
        val questionReview = questionReviewRepository.findByIdAndUserId(reviewId, userId)
        questionReview.delete()
        questionReviewRepository.save(questionReview)
        applicationEventPublisher.publishEvent(
            DeletedReviewEvent.create(questionReview.questionId, questionReview.rate)
        )
    }
}
