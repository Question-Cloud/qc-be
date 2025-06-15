package com.eager.questioncloud.workspace.dto

import com.eager.questioncloud.question.enums.Subject
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class UpdateCreatorProfileRequest(
    @NotNull val mainSubject: Subject,
    @NotBlank val introduction: String,
)
