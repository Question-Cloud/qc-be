package com.eager.questioncloud.application.api.payment.point.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.payment.point.dto.ChargePointOrderRequest
import com.eager.questioncloud.application.api.payment.point.dto.ChargePointPaymentRequest
import com.eager.questioncloud.application.api.payment.point.dto.CheckCompletePaymentResponse
import com.eager.questioncloud.application.business.payment.point.service.ChargePointPaymentHistoryService
import com.eager.questioncloud.application.business.payment.point.service.ChargePointPaymentService
import com.eager.questioncloud.application.business.payment.point.service.PgPaymentService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.point.dto.ChargePointPaymentHistory
import com.eager.questioncloud.core.domain.point.dto.ChargePointPaymentHistory.Companion.from
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment.Companion.order
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payment/point")
class ChargePointPaymentController(
    private val chargePointPaymentService: ChargePointPaymentService,
    private val pgPaymentService: PgPaymentService,
    private val chargePointPaymentHistoryService: ChargePointPaymentHistoryService,
) {
    @GetMapping("/status/{paymentId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "포인트 충전 완료 여부 조회",
        summary = "포인트 충전 완료 여부 조회",
        tags = ["charge-point"],
        description = "포인트 충전 완료 여부 조회"
    )
    fun isCompletePayment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable paymentId: String
    ): CheckCompletePaymentResponse {
        val isCompletePayment = chargePointPaymentService.isCompletePayment(userPrincipal.user.uid!!, paymentId)
        return CheckCompletePaymentResponse(isCompletePayment)
    }

    @PostMapping("/order")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "포인트 충전 주문 생성", summary = "포인트 충전 주문 생성", tags = ["charge-point"], description = """
                포인트 충전 주문을 생성 합니다.
                포트원 결제창 호출 전 필수로 요청을 해야 합니다.
            """
    )
    fun createOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody chargePointOrderRequest: ChargePointOrderRequest
    ): DefaultResponse {
        chargePointPaymentService.createOrder(
            order(
                chargePointOrderRequest.paymentId,
                userPrincipal.user.uid!!,
                chargePointOrderRequest.chargePointType
            )
        )
        return success()
    }

    @PostMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "Portone 포인트 충전 Webhook",
        summary = "Portone 포인트 충전 Webhook",
        tags = ["charge-point"],
        description = "Portone 포인트 충전 Webhook"
    )
    fun payment(@RequestBody request: ChargePointPaymentRequest): DefaultResponse {
        val pgPayment = pgPaymentService.getPgPayment(request.payment_id)
        chargePointPaymentService.approvePayment(pgPayment)
        return success()
    }

    @GetMapping("/history")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "포인트 충전 내역 조회",
        summary = "포인트 충전 내역 조회",
        tags = ["charge-point"],
        description = "포인트 충전 내역 조회"
    )
    fun getChargePointHistory(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, pagingInformation: PagingInformation
    ): PagingResponse<ChargePointPaymentHistory> {
        return PagingResponse(
            chargePointPaymentHistoryService.countChargePointPayment(userPrincipal.user.uid!!),
            from(
                chargePointPaymentHistoryService.getChargePointPayments(userPrincipal.user.uid!!, pagingInformation)
            )
        )
    }
}
