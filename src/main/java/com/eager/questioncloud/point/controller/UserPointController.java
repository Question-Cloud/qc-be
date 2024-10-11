package com.eager.questioncloud.point.controller;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.point.dto.Request.ChargePointRequest;
import com.eager.questioncloud.point.dto.Response.GetUserPointResponse;
import com.eager.questioncloud.point.service.UserPointService;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
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

    @PostMapping("/charge")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "포인트 충전", summary = "포인트 충전", tags = {"point"}, description = "포인트 충전")
    public DefaultResponse chargePoint(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid ChargePointRequest chargePointRequest) {
        userPointService.chargePoint(userPrincipal.getUser().getUid(), chargePointRequest.getChargePointType(), chargePointRequest.getPaymentId());
        return DefaultResponse.success();
    }
}
