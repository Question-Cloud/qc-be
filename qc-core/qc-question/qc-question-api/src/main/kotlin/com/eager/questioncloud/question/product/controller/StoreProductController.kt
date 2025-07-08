package com.eager.questioncloud.question.product.controller

import com.eager.questioncloud.common.auth.UserPrincipal
import com.eager.questioncloud.common.dto.PagingResponse
import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.question.common.QuestionFilter
import com.eager.questioncloud.question.product.dto.CategoriesResponse
import com.eager.questioncloud.question.product.dto.StoreProductDetail
import com.eager.questioncloud.question.product.dto.StoreProductDetailResponse
import com.eager.questioncloud.question.product.service.StoreProductService
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
    fun getQuestionListByFiltering(
        userPrincipal: UserPrincipal,
        questionFilter: QuestionFilter,
        pagingInformation: PagingInformation
    ): PagingResponse<StoreProductDetail> {
        val total = storeProductService.getTotalFiltering(questionFilter)
        val questionInformation =
            storeProductService.getQuestionListByFiltering(userPrincipal.userId, questionFilter, pagingInformation)
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
