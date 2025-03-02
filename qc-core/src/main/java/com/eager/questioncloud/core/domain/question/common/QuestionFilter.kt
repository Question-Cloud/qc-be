package com.eager.questioncloud.core.domain.question.common

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.question.enums.QuestionLevel
import com.eager.questioncloud.core.domain.question.enums.QuestionSortType
import com.eager.questioncloud.core.domain.question.enums.QuestionType

class QuestionFilter(
    val userId: Long? = null,
    val categories: List<Long>? = null,
    val levels: List<QuestionLevel>? = null,
    val questionType: QuestionType? = null,
    val creatorId: Long? = null,
    val sort: QuestionSortType? = null,
    val pagingInformation: PagingInformation? = null
) {

}
