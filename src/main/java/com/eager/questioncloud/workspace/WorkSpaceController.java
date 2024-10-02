package com.eager.questioncloud.workspace;

import com.eager.questioncloud.board.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.question.Request.ModifySelfMadeQuestionRequest;
import com.eager.questioncloud.question.Request.RegisterSelfMadeQuestionRequest;
import com.eager.questioncloud.question.Response.QuestionContentResponse;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspace")
@RequiredArgsConstructor
public class WorkSpaceController {
    private final WorkSpaceQuestionService workSpaceQuestionService;
    private final WorkSpaceBoardService workSpaceBoardService;

    @GetMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 목록 조회", summary = "나의 자작 문제 목록 조회", tags = {"workspace"}, description = "나의 자작 문제 목록 조회")
    public PagingResponse<QuestionInformationForWorkSpace> getQuestions(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        int total = workSpaceQuestionService.count(userPrincipal.getCreator().getId());
        List<QuestionInformationForWorkSpace> questions = workSpaceQuestionService.getQuestions(userPrincipal.getCreator().getId(), pageable);
        return new PagingResponse<>(total, questions);
    }

    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 상세 조회", summary = "나의 자작 문제 상세 조회", tags = {"workspace"}, description = "나의 자작 문제 상세 조회")
    public QuestionContentResponse getQuestion(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionContent questionContent = workSpaceQuestionService.getQuestionContent(userPrincipal.getCreator().getId(), questionId);
        return new QuestionContentResponse(questionContent);
    }

    @PostMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 등록", summary = "나의 자작 문제 등록", tags = {"workspace"}, description = "나의 자작 문제 등록")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid RegisterSelfMadeQuestionRequest request) {
        workSpaceQuestionService.register(userPrincipal.getCreator().getId(), request.toModel());
        return DefaultResponse.success();
    }

    @PatchMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 수정", summary = "나의 자작 문제 수정", tags = {"workspace"}, description = "나의 자작 문제 수정")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId,
        @RequestBody @Valid ModifySelfMadeQuestionRequest request) {
        workSpaceQuestionService.modify(userPrincipal.getCreator().getId(), questionId, request.toModel());
        return DefaultResponse.success();
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 삭제", summary = "나의 자작 문제 삭제", tags = {"workspace"}, description = "나의 자작 문제 삭제")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        workSpaceQuestionService.delete(userPrincipal.getCreator().getId(), questionId);
        return DefaultResponse.success();
    }

    @GetMapping("/board")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 게시판 목록 조회", summary = "나의 자작 문제 게시판 목록 조회", tags = {"workspace"},
        description = """
                나의 자작 문제 게시판에 등록된 게시글들을 통합 조회 합니다.
            """)
    public PagingResponse<QuestionBoardListItem> creatorQuestionBoardList(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        int total = workSpaceBoardService.countCreatorQuestionBoardList(userPrincipal.getCreator().getId());
        List<QuestionBoardListItem> boards = workSpaceBoardService.getCreatorQuestionBoardList(userPrincipal.getCreator().getId(), pageable);
        return new PagingResponse<>(total, boards);
    }
}
