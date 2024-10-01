package com.eager.questioncloud.review;

import com.eager.questioncloud.common.DefaultResponse;
import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.review.QuestionReviewDto.MyQuestionReview;
import com.eager.questioncloud.review.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.review.Request.ModifyQuestionReviewRequest;
import com.eager.questioncloud.review.Request.RegisterQuestionReviewRequest;
import com.eager.questioncloud.review.Response.MyQuestionReviewResponse;
import com.eager.questioncloud.security.UserPrincipal;
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
    public MyQuestionReviewResponse getMyQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam Long questionId) {
        QuestionReview review = questionReviewService.getMyQuestionReview(questionId, userPrincipal.getUser().getUid());
        return new MyQuestionReviewResponse(MyQuestionReview.of(review));
    }

    @PostMapping
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
    public DefaultResponse modifyQuestionReview(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId, @RequestBody @Valid ModifyQuestionReviewRequest request) {
        questionReviewService.modify(reviewId, userPrincipal.getUser().getUid(), request.getComment(), request.getRate());
        return DefaultResponse.success();
    }

    @DeleteMapping("/{reviewId}")
    public DefaultResponse deleteQuestionReview(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long reviewId) {
        questionReviewService.delete(reviewId, userPrincipal.getUser().getUid());
        return DefaultResponse.success();
    }
}
