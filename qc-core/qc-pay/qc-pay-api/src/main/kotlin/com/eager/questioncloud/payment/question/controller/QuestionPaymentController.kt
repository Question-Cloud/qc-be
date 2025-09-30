package com.eager.questioncloud.payment.question.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.eager.questioncloud.payment.question.command.QuestionPaymentCommand
import com.eager.questioncloud.payment.question.dto.QuestionPaymentRequest
import com.eager.questioncloud.payment.question.service.QuestionPaymentHistoryService
import com.eager.questioncloud.payment.question.service.QuestionPaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/question")
class QuestionPaymentController(
    private val questionPaymentService: QuestionPaymentService,
    private val questionPaymentHistoryService: QuestionPaymentHistoryService,
) {
    @PostMapping
    fun questionPayment(
        userPrincipal: UserPrincipal,
        @RequestBody request: QuestionPaymentRequest
    ): DefaultResponse {
        questionPaymentService.payment(
            QuestionPaymentCommand(
                userPrincipal.userId,
                request.orders.map { it.toCommand() },
                request.userCouponId
            )
        )
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
