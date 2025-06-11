package com.eager.questioncloud.application.api.user.point.controller

import com.eager.questioncloud.application.api.user.point.dto.GetUserPointResponse
import com.eager.questioncloud.application.api.user.point.service.UserPointService
import com.eager.questioncloud.application.security.UserPrincipal
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
    fun getUserPoint(@AuthenticationPrincipal userPrincipal: UserPrincipal): GetUserPointResponse {
        val userPoint = userPointService.getUserPoint(userPrincipal.user.uid)
        return GetUserPointResponse(userPoint.point)
    }
}
