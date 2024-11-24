package com.eager.questioncloud.application.api.post;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.post.PostControllerRequest.ModifyPostRequest;
import com.eager.questioncloud.application.api.post.PostControllerRequest.RegisterPostRequest;
import com.eager.questioncloud.application.api.post.PostControllerResponse.PostResponse;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.post.Post;
import com.eager.questioncloud.core.domain.post.PostContent;
import com.eager.questioncloud.core.domain.post.PostDetail;
import com.eager.questioncloud.core.domain.post.PostListItem;
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
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PatchMapping("/{postId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 수정", summary = "문제 게시판 글 수정", tags = {"question-board"}, description = "문제 게시판 글 수정")
    public DefaultResponse modify(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId, @RequestBody @Valid ModifyPostRequest request) {
        postService.modify(
            postId,
            userPrincipal.getUser().getUid(),
            PostContent.create(request.getTitle(), request.getContent(), request.getFiles()));
        return DefaultResponse.success();
    }

    @DeleteMapping("/{postId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 삭제", summary = "문제 게시판 글 삭제", tags = {"question-board"}, description = "문제 게시판 글 삭제")
    public DefaultResponse delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        postService.delete(postId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }

    @GetMapping("/{postId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 조회", summary = "문제 게시판 글 조회", tags = {"question-board"}, description = "문제 게시판 글 조회")
    public PostResponse getPost(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long postId) {
        PostDetail board = postService.getPostDetail(userPrincipal.getUser().getUid(), postId);
        return new PostResponse(board);
    }

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 목록 조회", summary = "문제 게시판 글 목록 조회", tags = {"question-board"}, description = "문제 게시판 글 목록 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<PostListItem> getPosts(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId, PagingInformation pagingInformation) {
        int total = postService.countPost(questionId);
        List<PostListItem> boards = postService.getPostList(userPrincipal.getUser().getUid(), questionId, pagingInformation);
        return new PagingResponse<>(total, boards);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 게시판 글 등록", summary = "문제 게시판 글 등록", tags = {"question-board"}, description = "문제 게시판 글 등록")
    public DefaultResponse register(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterPostRequest request) {
        postService.register(
            Post.create(
                request.getQuestionId(),
                userPrincipal.getUser().getUid(),
                PostContent.create(request.getTitle(), request.getContent(), request.getFiles()))
        );
        return DefaultResponse.success();
    }
}
