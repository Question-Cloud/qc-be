package com.eager.questioncloud.point.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.point.dto.*
import com.eager.questioncloud.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.point.service.ChargePointPaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/point")
class ChargePointController(
    private val chargePointPaymentService: ChargePointPaymentService,
    private val chargePointPaymentHistoryService: ChargePointPaymentHistoryService,
) {
    @GetMapping("/status/{orderId}")
    fun isCompletePayment(
        userPrincipal: UserPrincipal, @PathVariable orderId: String
    ): CheckCompletePaymentResponse {
        val isCompletePayment = chargePointPaymentService.isCompletePayment(userPrincipal.userId, orderId)
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
    fun payment(@RequestBody request: ChargePointPaymentRequest): ChargePointPaymentResponse {
        val result = chargePointPaymentService.approvePayment(request.orderId)
        return ChargePointPaymentResponse(result)
    }
    
    @GetMapping("/history")
    fun getChargePointHistory(
        userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<ChargePointPaymentHistory> {
        return PagingResponse(
            chargePointPaymentHistoryService.countChargePointPayment(userPrincipal.userId),
            chargePointPaymentHistoryService.getChargePointPaymentHistory(userPrincipal.userId, pagingInformation)
        )
    }
}
