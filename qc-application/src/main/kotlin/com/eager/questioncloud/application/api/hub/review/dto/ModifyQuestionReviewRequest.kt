package com.eager.questioncloud.application.api.hub.review.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

class ModifyQuestionReviewRequest(
    @Min(value = 1) @Max(value = 5) val rate: Int,
    @NotBlank val comment: String,
)