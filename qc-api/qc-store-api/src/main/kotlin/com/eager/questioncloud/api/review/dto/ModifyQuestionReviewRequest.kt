package com.eager.questioncloud.api.review.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

class ModifyQuestionReviewRequest(
    @Min(value = 1) @Max(value = 5) val rate: Int,
    @NotBlank val comment: String,
)