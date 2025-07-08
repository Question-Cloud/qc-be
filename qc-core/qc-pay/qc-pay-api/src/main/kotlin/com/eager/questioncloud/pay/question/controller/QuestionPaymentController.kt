package com.eager.questioncloud.pay.question.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.pay.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.pay.question.service.QuestionOrderService
import com.eager.questioncloud.pay.question.service.QuestionPaymentCouponService
import com.eager.questioncloud.pay.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.pay.question.service.QuestionPaymentService
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
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
        userPrincipal: UserPrincipal,
        @RequestBody request: QuestionPaymentRequest
    ): DefaultResponse {
        val questionOrder = questionOrderService.generateQuestionOrder(userPrincipal.userId, request.questionIds!!)
        val questionPaymentCoupon = questionPaymentCouponService.getQuestionPaymentCoupon(
            request.userCouponId,
            userPrincipal.userId
        )
        questionPaymentService.payment(userPrincipal.userId, questionOrder, questionPaymentCoupon)
        return DefaultResponse.success()
    }

    @GetMapping("/history")
    fun getQuestionPaymentHistory(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<QuestionPaymentHistory> {
        return PagingResponse(
            questionPaymentHistoryService.countQuestionPaymentHistory(userPrincipal.userId),
            questionPaymentHistoryService.getQuestionPaymentHistory(userPrincipal.userId, pagingInformation)
        )
    }
}
