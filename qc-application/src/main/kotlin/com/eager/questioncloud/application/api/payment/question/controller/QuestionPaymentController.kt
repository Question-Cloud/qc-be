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
    fun questionPayment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody request: QuestionPaymentRequest
    ): DefaultResponse {
        val questionOrder = questionOrderService.generateQuestionOrder(userPrincipal.user.uid, request.questionIds!!)
        val questionPaymentCoupon = questionPaymentCouponService.getQuestionPaymentCoupon(
            request.userCouponId,
            userPrincipal.user.uid
        )
        questionPaymentService.payment(userPrincipal.user.uid, questionOrder, questionPaymentCoupon)
        return success()
    }

    @GetMapping("/history")
    fun getQuestionPaymentHistory(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<QuestionPaymentHistory> {
        return PagingResponse(
            questionPaymentHistoryService.countQuestionPaymentHistory(userPrincipal.user.uid),
            questionPaymentHistoryService.getQuestionPaymentHistory(userPrincipal.user.uid, pagingInformation)
        )
    }
}
