package com.eager.questioncloud.application.api.hub.review.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.hub.review.dto.ModifyQuestionReviewRequest
import com.eager.questioncloud.application.api.hub.review.dto.MyQuestionReviewResponse
import com.eager.questioncloud.application.api.hub.review.dto.RegisterQuestionReviewRequest
import com.eager.questioncloud.application.api.hub.review.service.HubReviewService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail
import com.eager.questioncloud.core.domain.review.model.QuestionReview.Companion.create
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/hub/question/review")
class HubReviewController(
    private val hubReviewService: HubReviewService
) {
    @GetMapping
    fun getQuestionReviews(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<QuestionReviewDetail> {
        val total = hubReviewService.count(questionId)
        val questionReviewDetails = hubReviewService.getQuestionReviewDetails(
            questionId,
            userPrincipal.user.uid,
            pagingInformation
        )
        return PagingResponse(total, questionReviewDetails)
    }

    @GetMapping("/me")
    fun getMyQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long
    ): MyQuestionReviewResponse {
        val review = hubReviewService.getMyQuestionReview(questionId, userPrincipal.user.uid)
        return MyQuestionReviewResponse(review)
    }

    @PostMapping
    fun registerQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionReviewRequest
    ): DefaultResponse {
        hubReviewService.register(
            create(
                request.questionId,
                userPrincipal.user.uid,
                request.comment,
                request.rate
            )
        )
        return success()
    }

    @PatchMapping("/{reviewId}")
    fun modifyQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable reviewId: Long,
        @RequestBody request: @Valid ModifyQuestionReviewRequest
    ): DefaultResponse {
        hubReviewService.modify(reviewId, userPrincipal.user.uid, request.comment, request.rate)
        return success()
    }

    @DeleteMapping("/{reviewId}")
    fun deleteQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable reviewId: Long
    ): DefaultResponse {
        hubReviewService.delete(reviewId, userPrincipal.user.uid)
        return success()
    }
}
