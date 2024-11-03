package com.eager.questioncloud.api.creator;

import com.eager.questioncloud.api.question.Response.QuestionContentResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.creator.service.CreatorWorkSpaceService;
import com.eager.questioncloud.core.domain.hub.board.dto.PostDto.PostListItem;
import com.eager.questioncloud.core.domain.hub.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class CreatorWorkSpaceController {
    private final CreatorWorkSpaceService creatorWorkSpaceService;

    @GetMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 목록 조회", summary = "나의 자작 문제 목록 조회", tags = {"workspace"}, description = "나의 자작 문제 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionInformation> getQuestions(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = creatorWorkSpaceService.countCreatorQuestionCount(userPrincipal.getCreator().getId());
        List<QuestionInformation> questions = creatorWorkSpaceService.getCreatorQuestions(userPrincipal.getCreator().getId(), pagingInformation);
        return new PagingResponse<>(total, questions);
    }

    @GetMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 상세 조회", summary = "나의 자작 문제 상세 조회", tags = {"workspace"}, description = "나의 자작 문제 상세 조회")
    public QuestionContentResponse getQuestion(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionContent questionContent = creatorWorkSpaceService.getQuestionContent(userPrincipal.getCreator().getId(), questionId);
        return new QuestionContentResponse(questionContent);
    }

    @PostMapping("/question")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 등록", summary = "나의 자작 문제 등록", tags = {"workspace"}, description = "나의 자작 문제 등록")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid Request.RegisterQuestionRequest request) {
        creatorWorkSpaceService.registerQuestion(userPrincipal.getCreator().getId(), request.toQuestionContent());
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
        @RequestBody @Valid Request.ModifyQuestionRequest request) {
        creatorWorkSpaceService.modifyQuestion(userPrincipal.getCreator().getId(), questionId, request.toQuestionContent());
        return DefaultResponse.success();
    }

    @DeleteMapping("/question/{questionId}")
    @PreAuthorize("hasAnyRole('ROLE_CreatorUser')")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "나의 자작 문제 삭제", summary = "나의 자작 문제 삭제", tags = {"workspace"}, description = "나의 자작 문제 삭제")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        creatorWorkSpaceService.deleteQuestion(userPrincipal.getCreator().getId(), questionId);
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
    public PagingResponse<PostListItem> creatorQuestionBoardList(
        @AuthenticationPrincipal UserPrincipal userPrincipal, PagingInformation pagingInformation) {
        int total = creatorWorkSpaceService.countCreatorQuestionBoardList(userPrincipal.getCreator().getId());
        List<PostListItem> boards = creatorWorkSpaceService.getCreatorQuestionBoardList(
            userPrincipal.getCreator().getId(),
            pagingInformation);
        return new PagingResponse<>(total, boards);
    }
}
