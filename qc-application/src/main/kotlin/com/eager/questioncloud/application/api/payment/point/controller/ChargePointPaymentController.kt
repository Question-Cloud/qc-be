package com.eager.questioncloud.application.api.payment.point.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.payment.point.dto.ChargePointOrderRequest
import com.eager.questioncloud.application.api.payment.point.dto.ChargePointOrderResponse
import com.eager.questioncloud.application.api.payment.point.dto.ChargePointPaymentRequest
import com.eager.questioncloud.application.api.payment.point.dto.CheckCompletePaymentResponse
import com.eager.questioncloud.application.api.payment.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.application.api.payment.point.service.ChargePointPaymentService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.point.dto.ChargePointPaymentHistory
import com.eager.questioncloud.core.domain.point.dto.ChargePointPaymentHistory.Companion.from
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/point")
class ChargePointPaymentController(
    private val chargePointPaymentService: ChargePointPaymentService,
    private val chargePointPaymentHistoryService: ChargePointPaymentHistoryService,
) {
    @GetMapping("/status/{paymentId}")
    fun isCompletePayment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable paymentId: String
    ): CheckCompletePaymentResponse {
        val isCompletePayment = chargePointPaymentService.isCompletePayment(userPrincipal.user.uid, paymentId)
        return CheckCompletePaymentResponse(isCompletePayment)
    }

    @PostMapping("/order")
    fun createOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody chargePointOrderRequest: ChargePointOrderRequest
    ): ChargePointOrderResponse {
        val orderId =
            chargePointPaymentService.createOrder(userPrincipal.user.uid, chargePointOrderRequest.chargePointType)
        return ChargePointOrderResponse(orderId)
    }

    @PostMapping
    fun payment(@RequestBody request: ChargePointPaymentRequest): DefaultResponse {
        chargePointPaymentService.approvePayment(request.orderId)
        return success()
    }

    @GetMapping("/history")
    fun getChargePointHistory(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<ChargePointPaymentHistory> {
        return PagingResponse(
            chargePointPaymentHistoryService.countChargePointPayment(userPrincipal.user.uid),
            from(
                chargePointPaymentHistoryService.getChargePointPayments(userPrincipal.user.uid, pagingInformation)
            )
        )
    }
}
