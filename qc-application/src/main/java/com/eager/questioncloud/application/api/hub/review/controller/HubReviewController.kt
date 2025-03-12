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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/hub/question/review")
class HubReviewController(
    private val hubReviewService: HubReviewService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 리뷰 목록 조회",
        summary = "문제 리뷰 목록 조회",
        tags = ["question-review"],
        description = "문제 리뷰 조회"
    )
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getQuestionReviews(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long,
        pagingInformation: PagingInformation
    ): PagingResponse<QuestionReviewDetail> {
        val total = hubReviewService.getTotal(questionId)
        val questionReviewDetails = hubReviewService.getQuestionReviews(
            questionId,
            userPrincipal.user.uid!!,
            pagingInformation
        )
        return PagingResponse(total, questionReviewDetails)
    }

    @GetMapping("/me")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "내가 작성한 리뷰 조회", summary = "내가 작성한 리뷰 조회", tags = ["question-review"], description = """
                작성한 리뷰가 있다면 review 정보를 응답으로 반환하며
                작성한 리뷰가 없다면 404를 반환합니다.
            
            """
    )
    fun getMyQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam questionId: Long
    ): MyQuestionReviewResponse {
        val review = hubReviewService.getMyQuestionReview(questionId, userPrincipal.user.uid!!)
        return MyQuestionReviewResponse(review)
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 리뷰 등록", summary = "문제 리뷰 등록", tags = ["question-review"], description = "문제 리뷰 등록")
    fun registerQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: @Valid RegisterQuestionReviewRequest
    ): DefaultResponse {
        hubReviewService.register(
            create(
                request.questionId,
                userPrincipal.user.uid!!,
                request.comment,
                request.rate
            )
        )
        return success()
    }

    @PatchMapping("/{reviewId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 리뷰 수정", summary = "문제 리뷰 수정", tags = ["question-review"], description = "문제 리뷰 수정")
    fun modifyQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @PathVariable reviewId: Long,
        @RequestBody request: @Valid ModifyQuestionReviewRequest
    ): DefaultResponse {
        hubReviewService.modify(reviewId, userPrincipal.user.uid!!, request.comment, request.rate)
        return success()
    }

    @DeleteMapping("/{reviewId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 리뷰 삭제", summary = "문제 리뷰 삭제", tags = ["question-review"], description = "문제 리뷰 삭제")
    fun deleteQuestionReview(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable reviewId: Long
    ): DefaultResponse {
        hubReviewService.delete(reviewId, userPrincipal.user.uid!!)
        return success()
    }
}
