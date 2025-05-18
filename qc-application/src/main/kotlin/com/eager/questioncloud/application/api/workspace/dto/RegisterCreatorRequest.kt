package com.eager.questioncloud.application.api.workspace.dto

import com.eager.questioncloud.core.domain.question.enums.Subject
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterCreatorRequest(
    @NotNull val mainSubject: Subject,
    @NotBlank val introduction: String,
)