package com.eager.questioncloud.application.api.hub.question.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.hub.question.dto.QuestionCategoriesResponse
import com.eager.questioncloud.application.api.hub.question.dto.QuestionInformationResponse
import com.eager.questioncloud.application.api.hub.question.service.HubQuestionService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hub/question")
class HubQuestionController(
    private val hubQuestionService: HubQuestionService,
) {
    @GetMapping
    fun getQuestionListByFiltering(questionFilter: QuestionFilter): PagingResponse<QuestionInformation> {
        val total = hubQuestionService.getTotalFiltering(questionFilter)
        val questionInformation = hubQuestionService.getQuestionListByFiltering(questionFilter)
        return PagingResponse(total, questionInformation)
    }

    @GetMapping("/categories")
    fun getQuestionCategories(): QuestionCategoriesResponse {
        val categories = hubQuestionService.getQuestionCategories()
        return QuestionCategoriesResponse(categories)
    }

    @GetMapping("/{questionId}")
    fun getQuestionDetail(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): QuestionInformationResponse {
        val questionInformation = hubQuestionService.getQuestionInformation(
            questionId,
            userPrincipal.user.uid
        )
        return QuestionInformationResponse(questionInformation)
    }
}
