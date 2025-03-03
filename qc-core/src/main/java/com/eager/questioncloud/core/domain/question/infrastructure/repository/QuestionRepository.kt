package com.eager.questioncloud.core.domain.question.infrastructure.repository

import com.eager.questioncloud.core.common.PagingInformation
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import com.eager.questioncloud.core.domain.question.model.Question

interface QuestionRepository {
    fun countByQuestionFilter(questionFilter: QuestionFilter): Int

    fun getQuestionInformation(questionFilter: QuestionFilter): List<QuestionInformation>

    fun getQuestionInformation(questionId: Long, userId: Long): QuestionInformation

    fun getQuestionsByQuestionIds(questionIds: List<Long>): List<Question>

    fun isAvailable(questionId: Long): Boolean

    fun findByQuestionIdAndCreatorId(questionId: Long, creatorId: Long): Question

    fun get(questionId: Long): Question

    fun save(question: Question): Question

    fun findByCreatorIdWithPaging(creatorId: Long, pagingInformation: PagingInformation): List<QuestionInformation>

    fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionInformation>

    fun countByCreatorId(creatorId: Long): Int

    fun increaseQuestionCount(questionId: Long)

    fun deleteAllInBatch()
}
