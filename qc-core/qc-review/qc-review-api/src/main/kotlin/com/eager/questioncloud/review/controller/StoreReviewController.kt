package com.eager.questioncloud.review.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.review.command.DeleteReviewCommand
import com.eager.questioncloud.review.command.ModifyReviewCommand
import com.eager.questioncloud.review.command.RegisterReviewCommand
import com.eager.questioncloud.review.dto.ModifyQuestionReviewRequest
import com.eager.questioncloud.review.dto.MyQuestionReviewResponse
import com.eager.questioncloud.review.dto.QuestionReviewDetail
import com.eager.questioncloud.review.dto.RegisterQuestionReviewRequest
import com.eager.questioncloud.review.service.StoreReviewService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/store/review")
class StoreReviewController(
    private val storeReviewService: StoreReviewService
) {
    @GetMapping
    fun getQuestionReviews(
        userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<QuestionReviewDetail> {
        val total = storeReviewService.count(questionId)
        val questionReviewDetails = storeReviewService.getReviewDetails(
            questionId,
            userPrincipal.userId,
            pagingInformation
        )
        return PagingResponse(total, questionReviewDetails)
    }
    
    @GetMapping("/me")
    fun getMyQuestionReview(
        userPrincipal: UserPrincipal,
        @RequestParam questionId: Long
    ): MyQuestionReviewResponse {
        val review = storeReviewService.getMyReview(questionId, userPrincipal.userId)
        return MyQuestionReviewResponse(review)
    }
    
    @PostMapping
    fun registerQuestionReview(
        userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionReviewRequest
    ): DefaultResponse {
        storeReviewService.register(
            RegisterReviewCommand(userPrincipal.userId, request.questionId, request.comment, request.rate)
        )
        return DefaultResponse.success()
    }
    
    @PatchMapping("/{reviewId}")
    fun modifyQuestionReview(
        userPrincipal: UserPrincipal, @PathVariable reviewId: Long,
        @RequestBody request: @Valid ModifyQuestionReviewRequest
    ): DefaultResponse {
        storeReviewService.modify(
            ModifyReviewCommand(reviewId, userPrincipal.userId, request.comment, request.rate)
        )
        return DefaultResponse.success()
    }
    
    @DeleteMapping("/{reviewId}")
    fun deleteQuestionReview(
        userPrincipal: UserPrincipal,
        @PathVariable reviewId: Long
    ): DefaultResponse {
        storeReviewService.delete(
            DeleteReviewCommand(reviewId, userPrincipal.userId)
        )
        return DefaultResponse.success()
    }
}
