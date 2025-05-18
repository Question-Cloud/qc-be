package com.eager.questioncloud.application.api.user.profile.controller

import com.eager.questioncloud.application.api.common.DefaultResponse
import com.eager.questioncloud.application.api.common.DefaultResponse.Companion.success
import com.eager.questioncloud.application.api.user.profile.dto.MyInformationResponse
import com.eager.questioncloud.application.api.user.profile.dto.UpdateMyInformationRequest
import com.eager.questioncloud.application.api.user.profile.service.UserProfileService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.user.dto.MyInformation.Companion.from
import jakarta.validation.Valid
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/profile")
class UserProfileController(
    private val userService: UserProfileService
) {
    @GetMapping("/me")
    fun getMyInformation(@AuthenticationPrincipal userPrincipal: UserPrincipal): MyInformationResponse {
        return MyInformationResponse(from(userPrincipal.user))
    }

    @PatchMapping("/me")
    fun updateMyInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal, @RequestBody request: @Valid UpdateMyInformationRequest
    ): DefaultResponse {
        userService.updateUserInformation(userPrincipal.user, request.name, request.profileImage)
        return success()
    }
}
