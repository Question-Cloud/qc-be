package com.eager.questioncloud.application.api.hub.question.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.api.hub.question.dto.QuestionCategoriesResponse
import com.eager.questioncloud.application.api.hub.question.dto.QuestionInformationResponse
import com.eager.questioncloud.application.business.question.service.HubQuestionService
import com.eager.questioncloud.application.security.UserPrincipal
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springdoc.core.annotations.ParameterObject
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
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 목록 조회", summary = "문제 목록 조회", tags = ["question"], description = "문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getQuestionListByFiltering(@ParameterObject questionFilter: QuestionFilter): PagingResponse<QuestionInformation> {
        val total = hubQuestionService.getTotalFiltering(questionFilter)
        val questionInformation = hubQuestionService.getQuestionListByFiltering(questionFilter)
        return PagingResponse(total, questionInformation)
    }

    @GetMapping("/categories")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(
        operationId = "문제 카테고리 목록 조회",
        summary = "문제 카테고리 목록 조회",
        tags = ["question"],
        description = "문제 카테고리 목록 조회"
    )
    fun getQuestionCategories(): QuestionCategoriesResponse {
        val categories = hubQuestionService.getQuestionCategories()
        return QuestionCategoriesResponse(categories)
    }

    @GetMapping("/{questionId}")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "문제 상세 조회", summary = "문제 상세 조회", tags = ["question"], description = "문제 상세 조회")
    fun getQuestionDetail(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable questionId: Long
    ): QuestionInformationResponse {
        val questionInformation = hubQuestionService.getQuestionInformation(
            questionId,
            userPrincipal.user.uid!!
        )
        return QuestionInformationResponse(questionInformation)
    }
}
