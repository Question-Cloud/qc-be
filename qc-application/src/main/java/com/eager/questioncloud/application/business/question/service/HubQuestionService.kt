package com.eager.questioncloud.application.business.question.service

import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.Companion.create
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionCategoryRepository
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionRepository
import org.springframework.stereotype.Service

@Service
class HubQuestionService(
    private val questionRepository: QuestionRepository,
    private val questionCategoryRepository: QuestionCategoryRepository,
) {
    fun getTotalFiltering(questionFilter: QuestionFilter): Int {
        return questionRepository.countByQuestionFilter(questionFilter)
    }

    fun getQuestionListByFiltering(questionFilter: QuestionFilter): List<QuestionInformation> {
        return questionRepository.getQuestionInformation(questionFilter)
    }

    fun getQuestionInformation(questionId: Long, userId: Long): QuestionInformation {
        return questionRepository.getQuestionInformation(questionId, userId)
    }

    val questionCategories: List<QuestionCategoryGroupBySubject>
        get() {
            val mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories()
            return create(mainQuestionCategories)
        }
}
