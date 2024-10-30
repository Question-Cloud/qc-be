package com.eager.questioncloud.api.library;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.feed.library.service.UserQuestionLibraryService;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserQuestionLibraryController {
    private final UserQuestionLibraryService userQuestionLibraryService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 문제 목록 조회", summary = "나의 문제 목록 조회", tags = {"library"}, description = "나의 문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<UserQuestionLibraryItem> getUserQuestionLibraryList(@ParameterObject QuestionFilter questionFilter) {
        int total = userQuestionLibraryService.countUserQuestions(questionFilter);
        List<UserQuestionLibraryItem> userQuestions = userQuestionLibraryService.getUserQuestions(questionFilter);
        return new PagingResponse<>(total, userQuestions);
    }
}