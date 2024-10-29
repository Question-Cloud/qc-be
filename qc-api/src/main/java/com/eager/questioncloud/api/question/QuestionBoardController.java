package com.eager.questioncloud.api.question;

import com.eager.questioncloud.api.question.Response.QuestionBoardResponse;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardDto.QuestionBoardDetail;
import com.eager.questioncloud.core.domain.questionhub.board.dto.QuestionBoardDto.QuestionBoardListItem;
import com.eager.questioncloud.core.domain.questionhub.board.model.QuestionBoard;
import com.eager.questioncloud.core.domain.questionhub.board.service.QuestionBoardService;
import com.eager.questioncloud.core.domain.questionhub.board.vo.QuestionBoardContent;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class QuestionBoardController {
    private final QuestionBoardService questionBoardService;

    @PatchMapping("/{boardId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 수정", summary = "문제 게시판 글 수정", tags = {"question-board"}, description = "문제 게시판 글 수정")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId,
        @RequestBody @Valid Request.ModifyQuestionBoardRequest request) {
        questionBoardService.modify(
            boardId,
            userPrincipal.getUser().getUid(),
            QuestionBoardContent.create(request.getTitle(), request.getContent(), request.getFiles()));
        return DefaultResponse.success();
    }

    @DeleteMapping("/{boardId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 삭제", summary = "문제 게시판 글 삭제", tags = {"question-board"}, description = "문제 게시판 글 삭제")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId) {
        questionBoardService.delete(boardId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }

    @GetMapping("/{boardId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 조회", summary = "문제 게시판 글 조회", tags = {"question-board"}, description = "문제 게시판 글 조회")
    public QuestionBoardResponse getQuestionBoard(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long boardId) {
        QuestionBoardDetail board = questionBoardService.getQuestionBoardDetail(userPrincipal.getUser().getUid(), boardId);
        return new QuestionBoardResponse(board);
    }

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 목록 조회", summary = "문제 게시판 글 목록 조회", tags = {"question-board"}, description = "문제 게시판 글 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionBoardListItem> getQuestionBoards(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId, PagingInformation pagingInformation) {
        int total = questionBoardService.countQuestionBoard(questionId);
        List<QuestionBoardListItem> boards = questionBoardService.getQuestionBoardList(
            userPrincipal.getUser().getUid(),
            questionId,
            pagingInformation);
        return new PagingResponse<>(total, boards);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 등록", summary = "문제 게시판 글 등록", tags = {"question-board"}, description = "문제 게시판 글 등록")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody @Valid Request.RegisterQuestionBoardRequest request) {
        questionBoardService.register(
            QuestionBoard.create(
                request.getQuestionId(),
                userPrincipal.getUser().getUid(),
                QuestionBoardContent.create(request.getTitle(), request.getContent(), request.getFiles()))
        );
        return DefaultResponse.success();
    }
}
