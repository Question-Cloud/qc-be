package com.eager.questioncloud.domain.point.controller;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.domain.point.dto.Request.ChargePointOrderRequest;
import com.eager.questioncloud.domain.point.dto.Request.ChargePointPaymentRequest;
import com.eager.questioncloud.domain.point.dto.Response.CheckCompletePaymentResponse;
import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.domain.point.service.ChargePointService;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point/charge")
@RequiredArgsConstructor
public class ChargePointController {
    private final ChargePointService chargePointService;

    @GetMapping("/{paymentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "포인트 충전 완료 여부 조회", summary = "포인트 충전 완료 여부 조회", tags = {"charge-point"}, description = "포인트 충전 완료 여부 조회")
    public CheckCompletePaymentResponse isCompletePayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String paymentId) {
        Boolean isCompletePayment = chargePointService.isCompletePayment(userPrincipal.getUser().getUid(), paymentId);
        return new CheckCompletePaymentResponse(isCompletePayment);
    }

    @PostMapping("/order")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "포인트 충전 주문 생성", summary = "포인트 충전 주문 생성", tags = {"charge-point"},
        description = """
                포인트 충전 주문을 생성 합니다.
                포트원 결제창 호출 전 필수로 요청을 해야 합니다.
            """)
    public DefaultResponse createOrder(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody ChargePointOrderRequest chargePointOrderRequest) {
        chargePointService.createOrder(
            ChargePointOrder.crateOrder(
                userPrincipal.getCreator().getUserId(),
                chargePointOrderRequest.getPaymentId(),
                chargePointOrderRequest.getChargePointType())
        );
        return DefaultResponse.success();
    }

    @PostMapping("/payment")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "Portone 포인트 충전 Webhook", summary = "Portone 포인트 충전 Webhook", tags = {"charge-point"},
        description = "Portone 포인트 충전 Webhook")
    public DefaultResponse payment(@RequestBody ChargePointPaymentRequest request) {
        chargePointService.paymentAndCharge(request.getPayment_id());
        return DefaultResponse.success();
    }
}
