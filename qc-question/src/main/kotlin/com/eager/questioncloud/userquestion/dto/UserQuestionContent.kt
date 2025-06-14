package com.eager.questioncloud.userquestion.dto

import com.eager.questioncloud.question.enums.QuestionLevel

class UserQuestionContent(
    val questionId: Long,
    val creatorId: Long,
    val title: String,
    val parentCategory: String,
    val childCategory: String,
    val thumbnail: String,
    val questionLevel: QuestionLevel,
    val fileUrl: String,
    val explanationUrl: String,
)
