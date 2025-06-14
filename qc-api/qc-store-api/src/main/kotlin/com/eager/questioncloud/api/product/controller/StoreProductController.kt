package com.eager.questioncloud.api.product.controller

import com.eager.questioncloud.api.product.dto.CategoriesResponse
import com.eager.questioncloud.api.product.dto.StoreProductDetail
import com.eager.questioncloud.api.product.dto.StoreProductDetailResponse
import com.eager.questioncloud.api.product.service.StoreProductService
import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.question.common.QuestionFilter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/store/product")
class StoreProductController(
    private val storeProductService: StoreProductService,
) {
    @GetMapping
    fun getQuestionListByFiltering(questionFilter: QuestionFilter): PagingResponse<StoreProductDetail> {
        val total = storeProductService.getTotalFiltering(questionFilter)
        val questionInformation = storeProductService.getQuestionListByFiltering(questionFilter)
        return PagingResponse(total, questionInformation)
    }

    @GetMapping("/categories")
    fun getQuestionCategories(): CategoriesResponse {
        val categories = storeProductService.getQuestionCategories()
        return CategoriesResponse(categories)
    }

    @GetMapping("/{questionId}")
    fun getQuestionDetail(
        userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): StoreProductDetailResponse {
        val questionInformation = storeProductService.getQuestionInformation(
            questionId,
            userPrincipal.userId
        )
        return StoreProductDetailResponse(questionInformation)
    }
}
