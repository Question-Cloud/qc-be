package com.eager.questioncloud.application.api.payment.question.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.payment.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.application.api.payment.question.service.QuestionOrderService
import com.eager.questioncloud.application.api.payment.question.service.QuestionPaymentCouponService
import com.eager.questioncloud.application.api.payment.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.application.api.payment.question.service.QuestionPaymentService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/question")
class QuestionPaymentController(
    private val questionPaymentService: QuestionPaymentService,
    private val questionOrderService: QuestionOrderService,
    private val questionPaymentCouponService: QuestionPaymentCouponService,
    private val questionPaymentHistoryService: QuestionPaymentHistoryService,
) {
    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 구매", summary = "문제 구매", tags = ["question-payment"], description = "문제 구매")
    fun questionPayment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: QuestionPaymentRequest
    ): DefaultResponse {
        val questionOrder = questionOrderService.generateQuestionOrder(userPrincipal.user.uid!!, request.questionIds!!)
        val questionPaymentCoupon = questionPaymentCouponService.getQuestionPaymentCoupon(
            request.userCouponId,
            userPrincipal.user.uid!!
        )
        questionPaymentService.payment(userPrincipal.user.uid!!, questionOrder, questionPaymentCoupon)
        return success()
    }

    @GetMapping("/history")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 구매 내역 조회",
        summary = "문제 구매 내역 조회",
        tags = ["question-payment"],
        description = "문제 구매 내역 조회"
    )
    fun getQuestionPaymentHistory(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<QuestionPaymentHistory> {
        return PagingResponse(
            questionPaymentHistoryService.countQuestionPaymentHistory(userPrincipal.user.uid!!),
            questionPaymentHistoryService.getQuestionPaymentHistory(userPrincipal.user.uid!!, pagingInformation)
        )
    }
}
