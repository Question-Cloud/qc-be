package com.eager.questioncloud.comment;

import com.eager.questioncloud.comment.Request.AddQuestionBoardCommentRequest;
import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class QuestionBoardCommentController {
    private final QuestionBoardCommentService questionBoardCommentService;

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
}
