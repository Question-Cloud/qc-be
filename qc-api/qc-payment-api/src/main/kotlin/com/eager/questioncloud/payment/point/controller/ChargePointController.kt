package com.eager.questioncloud.payment.point.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.payment.point.dto.ChargePointOrderRequest
import com.eager.questioncloud.payment.point.dto.ChargePointOrderResponse
import com.eager.questioncloud.payment.point.dto.ChargePointPaymentRequest
import com.eager.questioncloud.payment.point.dto.CheckCompletePaymentResponse
import com.eager.questioncloud.payment.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.payment.point.service.ChargePointPaymentService
import com.eager.questioncloud.point.dto.ChargePointPaymentHistory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/point")
class ChargePointController(
    private val chargePointPaymentService: ChargePointPaymentService,
    private val chargePointPaymentHistoryService: ChargePointPaymentHistoryService,
) {
    @GetMapping("/status/{paymentId}")
    fun isCompletePayment(
        userPrincipal: UserPrincipal, @PathVariable paymentId: String
    ): CheckCompletePaymentResponse {
        val isCompletePayment = chargePointPaymentService.isCompletePayment(userPrincipal.userId, paymentId)
        return CheckCompletePaymentResponse(isCompletePayment)
    }

    @PostMapping("/order")
    fun createOrder(
        userPrincipal: UserPrincipal, @RequestBody chargePointOrderRequest: ChargePointOrderRequest
    ): ChargePointOrderResponse {
        val orderId =
            chargePointPaymentService.createOrder(userPrincipal.userId, chargePointOrderRequest.chargePointType)
        return ChargePointOrderResponse(orderId)
    }

    @PostMapping
    fun payment(@RequestBody request: ChargePointPaymentRequest): DefaultResponse {
        chargePointPaymentService.approvePayment(request.orderId)
        return DefaultResponse.success()
    }

    @GetMapping("/history")
    fun getChargePointHistory(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<ChargePointPaymentHistory> {
        return PagingResponse(
            chargePointPaymentHistoryService.countChargePointPayment(userPrincipal.userId),
            ChargePointPaymentHistory.from(
                chargePointPaymentHistoryService.getChargePointPayments(userPrincipal.userId, pagingInformation)
            )
        )
    }
}
