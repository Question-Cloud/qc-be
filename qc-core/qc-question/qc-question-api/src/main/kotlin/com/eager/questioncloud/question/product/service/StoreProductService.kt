package com.eager.questioncloud.question.product.service

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.dto.QuestionCategoryGroupBySubject
import com.eager.questioncloud.question.repository.QuestionCategoryRepository
import com.eager.questioncloud.question.product.dto.StoreProductDetail
import com.eager.questioncloud.question.product.implement.StoreProductDetailReader
import org.springframework.stereotype.Service

@Service
class StoreProductService(
    private val storeProductDetailReader: StoreProductDetailReader,
    private val questionCategoryRepository: QuestionCategoryRepository,
) {
    fun getTotalFiltering(questionFilter: QuestionFilter): Int {
        return storeProductDetailReader.count(questionFilter)
    }
    
    fun getQuestionListByFiltering(
        userId: Long,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): List<StoreProductDetail> {
        return storeProductDetailReader.getStoreProductDetails(userId, questionFilter, pagingInformation)
    }
    
    fun getQuestionInformation(questionId: Long, userId: Long): StoreProductDetail {
        return storeProductDetailReader.getStoreProductDetail(questionId, userId)
    }
    
    fun getQuestionCategories(): List<QuestionCategoryGroupBySubject> {
        val mainQuestionCategories = questionCategoryRepository.getMainQuestionCategories()
        return QuestionCategoryGroupBySubject.create(mainQuestionCategories)
    }
}
