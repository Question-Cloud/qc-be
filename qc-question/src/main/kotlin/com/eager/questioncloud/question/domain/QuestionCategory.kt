package com.eager.questioncloud.question.domain

import com.eager.questioncloud.question.enums.Subject

class QuestionCategory(
    val id: Long = 0,
    val parentId: Long,
    val subject: Subject,
    val title: String,
    val isParent: Boolean
)
