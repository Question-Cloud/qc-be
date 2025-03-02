package com.eager.questioncloud.core.domain.question.model

import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionType
import com.eager.questioncloud.core.domain.question.enums.Subject

class QuestionContent(
    val questionCategoryId: Long,
    val subject: Subject,
    val title: String,
    val description: String,
    val thumbnail: String,
    val fileUrl: String,
    val explanationUrl: String,
    val questionType: QuestionType,
    val questionLevel: QuestionLevel,
    val price: Int
)
