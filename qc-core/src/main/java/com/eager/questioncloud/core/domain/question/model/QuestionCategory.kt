package com.eager.questioncloud.core.domain.question.model

import com.eager.questioncloud.core.domain.question.enums.Subject

class QuestionCategory(
    val id: Long? = null,
    val parentId: Long,
    val subject: Subject,
    val title: String,
    val isParent: Boolean
)
