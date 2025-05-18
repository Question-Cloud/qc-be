package com.eager.questioncloud.application.api.post.dto

import com.eager.questioncloud.core.domain.post.model.PostFile
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import jakarta.validation.constraints.NotBlank

class ModifyPostRequest(
    @NotBlank val title: String,
    @NotBlank val content: String,
    @JsonSetter(nulls = Nulls.AS_EMPTY) val files: List<PostFile> = emptyList(),
)