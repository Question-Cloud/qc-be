package com.eager.questioncloud.post.dto

import com.eager.questioncloud.post.domain.PostFile
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class RegisterPostRequest(
    @NotNull val questionId: Long,
    @NotBlank val title: String,
    @NotBlank val content: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY) val files: List<PostFile> = ArrayList()
)