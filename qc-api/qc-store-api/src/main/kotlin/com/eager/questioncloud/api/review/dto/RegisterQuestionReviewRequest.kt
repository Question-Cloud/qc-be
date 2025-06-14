package com.eager.questioncloud.api.review.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterQuestionReviewRequest(
    @NotNull val questionId: Long,
    @Min(value = 1) @Max(value = 5) val rate: Int,
    @NotBlank val comment: String,
)