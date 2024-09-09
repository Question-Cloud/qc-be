package com.eager.questioncloud.comment;

import com.eager.questioncloud.comment.QuestionBoardCommentDto.QuestionBoardCommentDetail;
import com.eager.questioncloud.comment.Request.AddQuestionBoardCommentRequest;
import com.eager.questioncloud.comment.Request.ModifyQuestionBoardCommentRequest;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public PagingResponse<QuestionBoardCommentDetail> getQuestionBoardComments(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long boardId, Pageable pageable) {
        int total = questionBoardCommentService.count(boardId);
        List<QuestionBoardCommentDetail> comments = questionBoardCommentService.getQuestionBoardComments(
            boardId,
            userPrincipal.getUser().getUid(),
            pageable);
        return new PagingResponse<>(total, comments);
    }

    @PostMapping
    public DefaultResponse addQuestionBoardComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody AddQuestionBoardCommentRequest request) {
        questionBoardCommentService.addQuestionBoardComment(
            QuestionBoardComment.create(
                request.getBoardId(),
                userPrincipal.getUser().getUid(),
                request.getComment()));
        return DefaultResponse.success();
    }

    @PatchMapping("/{commentId}")
    public DefaultResponse modifyQuestionBoardComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId, @RequestBody ModifyQuestionBoardCommentRequest request) {
        questionBoardCommentService.modifyQuestionBoardComment(commentId, userPrincipal.getUser().getUid(), request.getComment());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{commentId}")
    public DefaultResponse deleteQuestionBoardComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        questionBoardCommentService.deleteQuestionBoardComment(commentId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
