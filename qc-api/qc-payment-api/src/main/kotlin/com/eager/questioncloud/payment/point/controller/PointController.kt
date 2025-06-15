package com.eager.questioncloud.payment.point.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.payment.point.dto.GetUserPointResponse
import com.eager.questioncloud.payment.point.service.PointService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment/point")
class PointController(
    private val pointService: PointService,
) {
    @GetMapping
    fun getUserPoint(userPrincipal: UserPrincipal): GetUserPointResponse {
        val userPoint = pointService.getUserPoint(userPrincipal.userId)
        return GetUserPointResponse(userPoint.point)
    }
}