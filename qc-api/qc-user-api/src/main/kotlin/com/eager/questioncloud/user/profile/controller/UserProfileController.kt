package com.eager.questioncloud.user.profile.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.DefaultResponse
import com.eager.questioncloud.user.profile.dto.MyInformationResponse
import com.eager.questioncloud.user.profile.dto.UpdateMyInformationRequest
import com.eager.questioncloud.user.profile.service.UserProfileService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/profile")
class UserProfileController(
    private val userProfileService: UserProfileService
) {
    @GetMapping("/me")
    fun getMyInformation(userPrincipal: UserPrincipal): MyInformationResponse {
        return MyInformationResponse(userProfileService.getMyInformation(userPrincipal.userId))
    }

    @PatchMapping("/me")
    fun updateMyInformation(
        userPrincipal: UserPrincipal, @RequestBody request: @Valid UpdateMyInformationRequest
    ): DefaultResponse {
        userProfileService.updateUserInformation(userPrincipal.userId, request.name, request.profileImage)
        return DefaultResponse.success()
    }
}
