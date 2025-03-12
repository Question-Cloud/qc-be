package com.eager.questioncloud.application.api.workspace.dto

import com.eager.questioncloud.core.domain.question.enums.Subject

class CreatorProfileResponse(
    val mainSubject: Subject,
    val introduction: String
)