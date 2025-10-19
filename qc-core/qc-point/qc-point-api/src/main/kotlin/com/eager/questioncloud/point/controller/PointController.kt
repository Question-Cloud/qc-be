package com.eager.questioncloud.point.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.point.dto.GetUserPointResponse
import com.eager.questioncloud.point.service.UserPointService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment/point")
class PointController(
    private val userPointService: UserPointService,
) {
    @GetMapping
    fun getUserPoint(userPrincipal: UserPrincipal): GetUserPointResponse {
        val userPoint = userPointService.getUserPoint(userPrincipal.userId)
        return GetUserPointResponse(userPoint.point)
    }
}