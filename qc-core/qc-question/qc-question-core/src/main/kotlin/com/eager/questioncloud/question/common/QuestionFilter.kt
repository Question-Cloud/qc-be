package com.eager.questioncloud.question.common

import com.eager.questioncloud.question.enums.QuestionLevel
import com.eager.questioncloud.question.enums.QuestionSortType
import com.eager.questioncloud.question.enums.QuestionType

class QuestionFilter(
    val categories: List<Long>? = null,
    val levels: List<QuestionLevel>? = null,
    val questionType: QuestionType? = null,
    val creatorId: Long? = null,
    val sort: QuestionSortType,
)
