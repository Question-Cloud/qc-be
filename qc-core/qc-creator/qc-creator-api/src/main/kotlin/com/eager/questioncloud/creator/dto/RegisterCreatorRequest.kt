package com.eager.questioncloud.creator.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterCreatorRequest(
    @NotNull val mainSubject: String,
    @NotBlank val introduction: String,
)