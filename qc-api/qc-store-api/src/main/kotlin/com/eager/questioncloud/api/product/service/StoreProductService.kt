package com.eager.questioncloud.api.product.service

import com.eager.questioncloud.api.product.dto.StoreProductDetail
import com.eager.questioncloud.api.product.implement.StoreProductDetailReader
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.QuestionCategoryGroupBySubject
import com.eager.questioncloud.question.infrastructure.repository.QuestionCategoryRepository
import org.springframework.stereotype.Service

@Service
class StoreProductService(
    private val questionHubDetailReader: StoreProductDetailReader,
    private val questionCategoryRepository: QuestionCategoryRepository,
) {
    fun getTotalFiltering(questionFilter: QuestionFilter): Int {
        return questionHubDetailReader.count(questionFilter)
    }

    fun getQuestionListByFiltering(questionFilter: QuestionFilter): List<StoreProductDetail> {
        return questionHubDetailReader.getQuestionHubDetails(questionFilter)
    }

    fun getQuestionInformation(questionId: Long, userId: Long): StoreProductDetail {
        return questionHubDetailReader.getQuestionHubDetail(questionId, userId)
    }

    fun getQuestionCategories(): List<QuestionCategoryGroupBySubject> {
        val mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories()
        return QuestionCategoryGroupBySubject.create(mainQuestionCategories)
    }
}
