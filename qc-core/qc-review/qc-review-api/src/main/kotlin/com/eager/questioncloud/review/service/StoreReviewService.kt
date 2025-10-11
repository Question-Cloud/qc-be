package com.eager.questioncloud.review.service

import com.eager.questioncloud.common.event.EventPublisher
import com.eager.questioncloud.common.event.ReviewEvent
import com.eager.questioncloud.common.event.ReviewEventType
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.command.DeleteReviewCommand
import com.eager.questioncloud.review.command.ModifyReviewCommand
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.dto.MyQuestionReview
import com.eager.questioncloud.review.dto.QuestionReviewDetail
import com.eager.questioncloud.review.implement.StoreReviewReader
import com.eager.questioncloud.review.implement.StoreReviewRegister
import com.eager.questioncloud.review.implement.StoreReviewRemover
import com.eager.questioncloud.review.implement.StoreReviewUpdater
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StoreReviewService(
    private val storeReviewReader: StoreReviewReader,
    private val storeReviewRegister: StoreReviewRegister,
    private val storeReviewUpdater: StoreReviewUpdater,
    private val storeReviewRemover: StoreReviewRemover,
    private val eventPublisher: EventPublisher,
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
    fun register(command: RegisterReviewCommand) {
        val questionReview = storeReviewRegister.register(command)
        eventPublisher.publish(ReviewEvent.create(questionReview.questionId, questionReview.rate, ReviewEventType.REGISTER))
    }
    
    @Transactional
    fun modify(command: ModifyReviewCommand) {
        val (questionId, varianceRate) = storeReviewUpdater.modify(command)
        eventPublisher.publish(ReviewEvent.create(questionId, varianceRate, ReviewEventType.MODIFY))
    }
    
    @Transactional
    fun delete(command: DeleteReviewCommand) {
        val (questionId, varianceRate) = storeReviewRemover.delete(command)
        eventPublisher.publish(ReviewEvent.create(questionId, varianceRate, ReviewEventType.DELETE))
    }
}
