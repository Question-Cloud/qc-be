package com.eager.questioncloud.application.api.post.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class AddPostCommentRequest(
    @NotNull val postId: Long,
    @NotBlank val comment: String,
)