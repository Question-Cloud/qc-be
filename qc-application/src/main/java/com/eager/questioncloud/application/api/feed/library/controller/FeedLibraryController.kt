package com.eager.questioncloud.application.api.feed.library.controller

import com.eager.questioncloud.application.api.common.PagingResponse
import com.eager.questioncloud.application.business.userquestion.service.UserQuestionService
import com.eager.questioncloud.core.domain.question.common.QuestionFilter
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springdoc.core.annotations.ParameterObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/feed/library")
class FeedLibraryController(
    private val userQuestionService: UserQuestionService
) {
    @GetMapping
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "요청 성공")])
    @Operation(operationId = "나의 문제 목록 조회", summary = "나의 문제 목록 조회", tags = ["library"], description = "나의 문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = Schema(type = "integer"))
    fun getUserQuestionLibraryList(@ParameterObject questionFilter: QuestionFilter): PagingResponse<UserQuestionDetail> {
        val total = userQuestionService.countUserQuestions(questionFilter)
        val userQuestions = userQuestionService.getUserQuestions(questionFilter)
        return PagingResponse(total, userQuestions)
    }
}
