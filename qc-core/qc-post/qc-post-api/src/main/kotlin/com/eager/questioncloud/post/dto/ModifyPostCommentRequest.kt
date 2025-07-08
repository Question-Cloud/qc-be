package com.eager.questioncloud.post.dto

import jakarta.validation.constraints.NotBlank

class ModifyPostCommentRequest(
    @NotBlank val comment: String = ""
)
