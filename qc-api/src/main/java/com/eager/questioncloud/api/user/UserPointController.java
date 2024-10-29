package com.eager.questioncloud.api.user;

import com.eager.questioncloud.api.user.Request.ChargePointOrderRequest;
import com.eager.questioncloud.api.user.Request.ChargePointPaymentRequest;
import com.eager.questioncloud.api.user.Response.CheckCompletePaymentResponse;
import com.eager.questioncloud.api.user.Response.GetUserPointResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import com.eager.questioncloud.core.domain.user.point.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.user.point.service.UserPointService;
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
@RequestMapping("/api/user/point")
@RequiredArgsConstructor
public class UserPointController {
    private final UserPointService userPointService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "보유중인 포인트 조회", summary = "보유중인 포인트 조회", tags = {"point"}, description = "보유중인 포인트 조회")
    public GetUserPointResponse getUserPoint(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userPoint = userPointService.getUserPoint(userPrincipal.getUser().getUid());
        return new GetUserPointResponse(userPoint);
    }

    @GetMapping("/charge/{paymentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "포인트 충전 완료 여부 조회", summary = "포인트 충전 완료 여부 조회", tags = {"charge-point"}, description = "포인트 충전 완료 여부 조회")
    public CheckCompletePaymentResponse isCompletePayment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable String paymentId) {
        Boolean isCompletePayment = userPointService.isCompletePayment(userPrincipal.getUser().getUid(), paymentId);
        return new CheckCompletePaymentResponse(isCompletePayment);
    }

    @PostMapping("/charge/order")
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
        userPointService.createOrder(
            ChargePointOrder.crateOrder(
                userPrincipal.getCreator().getUserId(),
                chargePointOrderRequest.getPaymentId(),
                chargePointOrderRequest.getChargePointType())
        );
        return DefaultResponse.success();
    }

    @PostMapping("/charge/payment")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "Portone 포인트 충전 Webhook", summary = "Portone 포인트 충전 Webhook", tags = {"charge-point"},
        description = "Portone 포인트 충전 Webhook")
    public DefaultResponse payment(@RequestBody ChargePointPaymentRequest request) {
        userPointService.paymentAndCharge(request.getPayment_id());
        return DefaultResponse.success();
    }
}
