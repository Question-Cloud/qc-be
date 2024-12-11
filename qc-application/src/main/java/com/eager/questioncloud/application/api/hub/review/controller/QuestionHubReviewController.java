package com.eager.questioncloud.application.api.hub.review.controller;

import com.eager.questioncloud.application.api.common.DefaultResponse;
import com.eager.questioncloud.application.api.common.PagingResponse;
import com.eager.questioncloud.application.api.hub.review.dto.QuestionHubReviewControllerRequest.ModifyQuestionReviewRequest;
import com.eager.questioncloud.application.api.hub.review.dto.QuestionHubReviewControllerRequest.RegisterQuestionReviewRequest;
import com.eager.questioncloud.application.api.hub.review.dto.QuestionHubReviewControllerResponse.MyQuestionReviewResponse;
import com.eager.questioncloud.application.api.hub.review.service.QuestionHubReviewService;
import com.eager.questioncloud.application.security.UserPrincipal;
import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.review.dto.MyQuestionReview;
import com.eager.questioncloud.core.domain.review.dto.QuestionReviewDetail;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
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
@RequestMapping("/api/hub/question/review")
@RequiredArgsConstructor
public class QuestionHubReviewController {
    private final QuestionHubReviewService questionHubReviewService;

    @GetMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 목록 조회", summary = "문제 리뷰 목록 조회", tags = {"question-review"}, description = "문제 리뷰 조회")
    @Parameter(name = "size", description = "paging size", schema = @Schema(type = "integer"))
    @Parameter(name = "page", description = "paging page", schema = @Schema(type = "integer"))
    public PagingResponse<QuestionReviewDetail> getQuestionReviews(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId, PagingInformation pagingInformation) {
        int total = questionHubReviewService.getTotal(questionId);
        List<QuestionReviewDetail> questionReviewDetails = questionHubReviewService.getQuestionReviews(
            questionId,
            userPrincipal.getUser().getUid(),
            pagingInformation);
        return new PagingResponse<>(total, questionReviewDetails);
    }

    @GetMapping("/me")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "내가 작성한 리뷰 조회", summary = "내가 작성한 리뷰 조회", tags = {"question-review"},
        description = """
                작성한 리뷰가 있다면 review 정보를 응답으로 반환하며
                작성한 리뷰가 없다면 404를 반환합니다.
            """)
    public MyQuestionReviewResponse getMyQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId) {
        MyQuestionReview review = questionHubReviewService.getMyQuestionReview(questionId, userPrincipal.getUser().getUid());
        return new MyQuestionReviewResponse(review);
    }

    @PostMapping
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 등록", summary = "문제 리뷰 등록", tags = {"question-review"}, description = "문제 리뷰 등록")
    public DefaultResponse registerQuestionReview(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Valid RegisterQuestionReviewRequest request) {
        questionHubReviewService.register(
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
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId,
        @RequestBody @Valid ModifyQuestionReviewRequest request) {
        questionHubReviewService.modify(reviewId, userPrincipal.getUser().getUid(), request.getComment(), request.getRate());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{reviewId}")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 리뷰 삭제", summary = "문제 리뷰 삭제", tags = {"question-review"}, description = "문제 리뷰 삭제")
    public DefaultResponse deleteQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId) {
        questionHubReviewService.delete(reviewId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
