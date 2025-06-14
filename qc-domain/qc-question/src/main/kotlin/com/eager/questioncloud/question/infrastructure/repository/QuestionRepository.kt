package com.eager.questioncloud.question.infrastructure.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.dto.QuestionInformation

interface QuestionRepository {
    fun countByQuestionFilter(questionFilter: QuestionFilter): Int

    fun getQuestionInformation(questionFilter: QuestionFilter): List<QuestionInformation>

    fun getQuestionInformation(questionId: Long, userId: Long = -1): QuestionInformation

    fun getQuestionsByQuestionIds(questionIds: List<Long>): List<Question>

    fun isAvailable(questionId: Long): Boolean

    fun findByQuestionIdAndCreatorId(questionId: Long, creatorId: Long): Question

    fun get(questionId: Long): Question

    fun save(question: Question): Question

    fun getQuestionInformationByCreatorIdWithPaging(
        creatorId: Long,
        pagingInformation: PagingInformation
    ): List<QuestionInformation>

    fun findByCreatorId(creatorId: Long): List<Question>

    fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionInformation>

    fun countByCreatorId(creatorId: Long): Int

    fun increaseQuestionCount(questionId: Long)

    fun deleteAllInBatch()
}
