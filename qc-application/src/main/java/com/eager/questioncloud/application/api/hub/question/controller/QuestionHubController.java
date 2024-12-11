package com.eager.questioncloud.application.api.hub.question.controller;

import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.hub.question.dto.QuestionHubControllerResponse.QuestionCategoriesResponse;
import com.eager.questioncloud.application.api.hub.question.dto.QuestionHubControllerResponse.QuestionInformationResponse;
import com.eager.questioncloud.application.api.hub.question.service.QuestionHubService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hub/question")
@RequiredArgsConstructor
public class QuestionHubController {
    private final QuestionHubService questionHubService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 목록 조회", summary = "문제 목록 조회", tags = {"question"}, description = "문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionInformation> getQuestionListByFiltering(@ParameterObject QuestionFilter questionFilter) {
        int total = questionHubService.getTotalFiltering(questionFilter);
        List<QuestionInformation> questionInformation = questionHubService.getQuestionListByFiltering(questionFilter);
        return new PagingResponse<>(total, questionInformation);
    }

    @GetMapping("/categories")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 카테고리 목록 조회", summary = "문제 카테고리 목록 조회", tags = {"question"}, description = "문제 카테고리 목록 조회")
    public QuestionCategoriesResponse getQuestionCategories() {
        List<QuestionCategoryGroupBySubject> categories = questionHubService.getQuestionCategories();
        return new QuestionCategoriesResponse(categories);
    }

    @GetMapping("/{questionId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 상세 조회", summary = "문제 상세 조회", tags = {"question"}, description = "문제 상세 조회")
    public QuestionInformationResponse getQuestionDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionInformation questionInformation = questionHubService.getQuestionInformation(questionId, userPrincipal.getUser().getUid());
        return new QuestionInformationResponse(questionInformation);
    }
}
