package com.eager.questioncloud.api.review.implement

import com.eager.questioncloud.api.review.dto.MyQuestionReview
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.dto.QuestionReviewDetail
import com.eager.questioncloud.review.infrastructure.repository.QuestionReviewRepository
import com.eager.questioncloud.user.infrastructure.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class StoreReviewReader(
    private val questionReviewRepository: QuestionReviewRepository,
    private val userRepository: UserRepository,
) {
    fun count(questionId: Long): Int {
        return questionReviewRepository.countByQuestionId(questionId)
    }

    fun getQuestionReviewDetails(
        questionId: Long,
        userId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionReviewDetail> {
        val reviews = questionReviewRepository.findByQuestionIdWithPagination(questionId, pagingInformation)
        val reviewerUserIds = reviews.map { it.reviewerId }
        val reviewerStatisticsMap = questionReviewRepository.getReviewerStatistics(reviewerUserIds)
        val reviewerUserMap = userRepository.findByUidIn(reviewerUserIds).associateBy { it.uid }

        return reviews.map {
            val reviewerUser = reviewerUserMap.getValue(it.reviewerId)

            QuestionReviewDetail(
                it.id,
                reviewerUser.userInformation.name,
                reviewerStatisticsMap.getValue(it.reviewerId),
                it.rate,
                it.comment,
                it.reviewerId == userId,
                it.createdAt
            )
        }
    }

    fun getMyQuestionReview(questionId: Long, userId: Long): MyQuestionReview {
        val questionReview = questionReviewRepository.findByQuestionIdAndUserId(questionId, userId)
        return MyQuestionReview.from(questionReview)
    }
}