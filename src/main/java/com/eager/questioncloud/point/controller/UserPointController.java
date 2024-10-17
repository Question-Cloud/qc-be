package com.eager.questioncloud.point.controller;

import com.eager.questioncloud.point.dto.Response.GetUserPointResponse;
import com.eager.questioncloud.point.service.UserPointService;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
}
