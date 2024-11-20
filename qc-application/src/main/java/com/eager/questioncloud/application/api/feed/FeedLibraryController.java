package com.eager.questioncloud.application.api.feed;

import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.domain.question.QuestionFilter;
import com.eager.questioncloud.domain.userquestion.UserQuestionDetail;
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
@RequestMapping("/api/feed/library")
@RequiredArgsConstructor
public class FeedLibraryController {
    private final FeedLibraryService feedLibraryService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 문제 목록 조회", summary = "나의 문제 목록 조회", tags = {"library"}, description = "나의 문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<UserQuestionDetail> getUserQuestionLibraryList(@ParameterObject QuestionFilter questionFilter) {
        int total = feedLibraryService.countUserQuestions(questionFilter);
        List<UserQuestionDetail> userQuestions = feedLibraryService.getUserQuestions(questionFilter);
        return new PagingResponse<>(total, userQuestions);
    }
}
