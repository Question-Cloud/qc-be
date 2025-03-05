package com.eager.questioncloud.application.api.user.point.controller

import com.eager.questioncloud.application.api.user.point.dto.GetUserPointResponse
import com.eager.questioncloud.application.business.point.service.UserPointService
import com.eager.questioncloud.application.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/point")
class UserPointController(
    private val userPointService: UserPointService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "보유중인 포인트 조회", summary = "보유중인 포인트 조회", tags = ["point"], description = "보유중인 포인트 조회")
    fun getUserPoint(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetUserPointResponse {
        val userPoint = userPointService.getUserPoint(userPrincipal.user.uid!!)
        return GetUserPointResponse(userPoint.point)
    }
}
