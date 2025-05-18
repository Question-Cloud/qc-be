package com.eager.questioncloud.application.api.post.dto

import jakarta.validation.constraints.NotBlank

class ModifyPostCommentRequest(
    @NotBlank val comment: String = ""
)
