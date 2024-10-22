package com.eager.questioncloud.domain.review.controller;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.MyQuestionReview;
import com.eager.questioncloud.domain.review.dto.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.domain.review.dto.Request.ModifyQuestionReviewRequest;
import com.eager.questioncloud.domain.review.dto.Request.RegisterQuestionReviewRequest;
import com.eager.questioncloud.domain.review.dto.Response.MyQuestionReviewResponse;
import com.eager.questioncloud.domain.review.model.QuestionReview;
import com.eager.questioncloud.domain.review.service.QuestionReviewService;
import com.eager.questioncloud.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
@RequestMapping("/api/question/review")
@RequiredArgsConstructor
public class QuestionReviewController {
    private final QuestionReviewService questionReviewService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 목록 조회", summary = "문제 리뷰 목록 조회", tags = {"question-review"}, description = "문제 리뷰 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionReviewItem> getQuestionReviews(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId, Pageable pageable) {
        int total = questionReviewService.getTotal(questionId);
        List<QuestionReviewItem> questionReviewItems = questionReviewService.getQuestionReviews(
            questionId,
            userPrincipal.getUser().getUid(),
            pageable);
        return new PagingResponse<>(total, questionReviewItems);
    }

    @GetMapping("/me")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "내가 작성한 리뷰 조회", summary = "내가 작성한 리뷰 조회", tags = {"question-review"},
        description = """
                작성한 리뷰가 있다면 review 정보를 응답으로 반환하며
                작성한 리뷰가 없다면 null를 반환합니다.
            """)
    public MyQuestionReviewResponse getMyQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId) {
        QuestionReview review = questionReviewService.getMyQuestionReview(questionId, userPrincipal.getUser().getUid());
        return new MyQuestionReviewResponse(MyQuestionReview.from(review));
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 등록", summary = "문제 리뷰 등록", tags = {"question-review"}, description = "문제 리뷰 등록")
    public DefaultResponse registerQuestionReview(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterQuestionReviewRequest request) {
        questionReviewService.register(
            QuestionReview.create(
                request.getQuestionId(),
                userPrincipal.getUser().getUid(),
                request.getComment(),
                request.getRate())
        );
        return DefaultResponse.success();
    }

    @PatchMapping("/{reviewId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 수정", summary = "문제 리뷰 수정", tags = {"question-review"}, description = "문제 리뷰 수정")
    public DefaultResponse modifyQuestionReview(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId, @RequestBody @Valid ModifyQuestionReviewRequest request) {
        questionReviewService.modify(reviewId, userPrincipal.getUser().getUid(), request.getComment(), request.getRate());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{reviewId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 삭제", summary = "문제 리뷰 삭제", tags = {"question-review"}, description = "문제 리뷰 삭제")
    public DefaultResponse deleteQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId) {
        questionReviewService.delete(reviewId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
