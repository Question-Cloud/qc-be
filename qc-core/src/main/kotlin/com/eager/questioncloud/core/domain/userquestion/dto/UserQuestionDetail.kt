package com.eager.questioncloud.core.domain.userquestion.dto

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel

class UserQuestionDetail(
    val questionId: Long,
    val title: String,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val creatorName: String,
    val questionLevel: QuestionLevel,
    val fileUrl: String,
    val explanationUrl: String,
)
