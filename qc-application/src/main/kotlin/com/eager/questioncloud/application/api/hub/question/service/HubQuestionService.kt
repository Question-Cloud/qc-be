package com.eager.questioncloud.application.api.hub.question.service

import com.eager.questioncloud.application.api.hub.question.dto.QuestionHubDetail
import com.eager.questioncloud.application.api.hub.question.implement.QuestionHubDetailReader
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.Companion.create
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionCategoryRepository
import org.springframework.stereotype.Service

@Service
class HubQuestionService(
    private val questionHubDetailReader: QuestionHubDetailReader,
    private val questionCategoryRepository: QuestionCategoryRepository,
) {
    fun getTotalFiltering(questionFilter: QuestionFilter): Int {
        return questionHubDetailReader.count(questionFilter)
    }

    fun getQuestionListByFiltering(questionFilter: QuestionFilter): List<QuestionHubDetail> {
        return questionHubDetailReader.getQuestionHubDetails(questionFilter)
    }

    fun getQuestionInformation(questionId: Long, userId: Long): QuestionHubDetail {
        return questionHubDetailReader.getQuestionHubDetail(questionId, userId)
    }

    fun getQuestionCategories(): List<QuestionCategoryGroupBySubject> {
        val mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories()
        return create(mainQuestionCategories)
    }
}
