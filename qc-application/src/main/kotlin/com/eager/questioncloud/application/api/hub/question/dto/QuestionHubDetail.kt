package com.eager.questioncloud.application.api.hub.question.dto

import com.eager.questioncloud.core.domain.question.dto.QuestionInformation

class QuestionHubDetail(
    val questionContent: QuestionInformation,
    val creator: String,
    val isOwned: Boolean,
)
