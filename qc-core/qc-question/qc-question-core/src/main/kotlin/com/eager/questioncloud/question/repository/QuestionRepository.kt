package com.eager.questioncloud.question.repository

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.domain.Question
import com.eager.questioncloud.question.dto.QuestionInformation

interface QuestionRepository {
    fun countByQuestionFilter(questionFilter: QuestionFilter): Int
    
    fun getQuestionInformation(
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<QuestionInformation>
    
    fun getQuestionInformation(questionId: Long): QuestionInformation // TODO Del userId Param
    
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
    
    fun findByQuestionIdIn(questionIds: List<Long>): List<QuestionInformation> // TODO Refactor Fun Name
    
    fun countByCreatorId(creatorId: Long): Int
    
    fun increaseQuestionCount(questionId: Long)
    
    fun deleteAllInBatch()
}
