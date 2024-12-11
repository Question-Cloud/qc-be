package com.eager.questioncloud.application.api.post.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.post.dto.PostCommentControllerRequest.AddPostCommentRequest;
import com.eager.questioncloud.application.api.post.dto.PostCommentControllerRequest.ModifyPostCommentRequest;
import com.eager.questioncloud.application.api.post.service.PostCommentService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.dto.PostCommentDetail;
import com.eager.questioncloud.core.domain.post.model.PostComment;
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
@RequestMapping("/api/post/comment")
@RequiredArgsConstructor
public class PostCommentController {
    private final PostCommentService postCommentService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 조회", summary = "문제 게시글 댓글 조회", tags = {"question-board-comment"}, description = "문제 게시글 댓글 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<PostCommentDetail> getPostComments(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long postId, PagingInformation pagingInformation) {
        int total = postCommentService.count(postId);
        List<PostCommentDetail> comments = postCommentService.getPostComments(postId, userPrincipal.getUser().getUid(), pagingInformation);
        return new PagingResponse<>(total, comments);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 작성", summary = "문제 게시글 댓글 작성", tags = {"question-board-comment"}, description = "문제 게시글 댓글 작성")
    public DefaultResponse addPostComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid AddPostCommentRequest request) {
        postCommentService.addPostComment(PostComment.create(request.getPostId(), userPrincipal.getUser().getUid(), request.getComment()));
        return DefaultResponse.success();
    }

    @PatchMapping("/{commentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 수정", summary = "문제 게시글 댓글 수정", tags = {"question-board-comment"}, description = "문제 게시글 댓글 수정")
    public DefaultResponse modifyPostComment(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId,
        @RequestBody @Valid ModifyPostCommentRequest request) {
        postCommentService.modifyPostComment(commentId, userPrincipal.getUser().getUid(), request.getComment());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{commentId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시글 댓글 삭제", summary = "문제 게시글 댓글 삭제", tags = {"question-board-comment"}, description = "문제 게시글 댓글 삭제")
    public DefaultResponse deletePostComment(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long commentId) {
        postCommentService.deletePostComment(commentId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
