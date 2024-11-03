package com.eager.questioncloud.api.question;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.hub.board.dto.PostCommentDto.PostCommentDetail;
import com.eager.questioncloud.core.domain.hub.board.model.QuestionBoardComment;
import com.eager.questioncloud.core.domain.hub.board.service.QuestionBoardCommentService;
import com.eager.questioncloud.security.UserPrincipal;
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
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class QuestionBoardCommentController {
    private final QuestionBoardCommentService questionBoardCommentService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 조회", summary = "문제 게시글 댓글 조회", tags = {"question-board-comment"}, description = "문제 게시글 댓글 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<PostCommentDetail> getQuestionBoardComments(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long boardId, PagingInformation pagingInformation) {
        int total = questionBoardCommentService.count(boardId);
        List<PostCommentDetail> comments = questionBoardCommentService.getQuestionBoardComments(
            boardId,
            userPrincipal.getUser().getUid(),
            pagingInformation);
        return new PagingResponse<>(total, comments);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 작성", summary = "문제 게시글 댓글 작성", tags = {"question-board-comment"}, description = "문제 게시글 댓글 작성")
    public DefaultResponse addQuestionBoardComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid Request.AddQuestionBoardCommentRequest request) {
        questionBoardCommentService.addQuestionBoardComment(
            QuestionBoardComment.create(
                request.getBoardId(),
                userPrincipal.getUser().getUid(),
                request.getComment()));
        return DefaultResponse.success();
    }

    @PatchMapping("/{commentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 수정", summary = "문제 게시글 댓글 수정", tags = {"question-board-comment"}, description = "문제 게시글 댓글 수정")
    public DefaultResponse modifyQuestionBoardComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId,
        @RequestBody @Valid Request.ModifyQuestionBoardCommentRequest request) {
        questionBoardCommentService.modifyQuestionBoardComment(commentId, userPrincipal.getUser().getUid(), request.getComment());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{commentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 삭제", summary = "문제 게시글 댓글 삭제", tags = {"question-board-comment"}, description = "문제 게시글 댓글 삭제")
    public DefaultResponse deleteQuestionBoardComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        questionBoardCommentService.deleteQuestionBoardComment(commentId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
