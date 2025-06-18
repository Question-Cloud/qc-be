package com.eager.questioncloud.user.profile.dto

import jakarta.validation.constraints.NotBlank

class UpdateMyInformationRequest(
    @NotBlank val profileImage: String = "default",
    @NotBlank val name: String,
) {
}
