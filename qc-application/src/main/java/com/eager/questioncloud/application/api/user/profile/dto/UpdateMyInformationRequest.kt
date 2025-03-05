package com.eager.questioncloud.application.api.user.profile.dto

import jakarta.validation.constraints.NotBlank

class UpdateMyInformationRequest(
    val profileImage: String?,
    @NotBlank val name: String,
) {
}
