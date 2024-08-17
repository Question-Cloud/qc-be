package com.eager.questioncloud.question;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionReviewDto.QuestionReviewItem;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question/review")
@RequiredArgsConstructor
public class QuestionReviewController {
    private final QuestionReviewService questionReviewService;

    @GetMapping("/{questionId}")
    public PagingResponse<QuestionReviewItem> getQuestionReviews(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId, Pageable pageable) {
        int total = questionReviewService.getTotal(questionId);
        List<QuestionReviewItem> questionReviewItems = questionReviewService.getQuestionReviews(
            questionId,
            userPrincipal.getUser().getUid(),
            pageable);
        return new PagingResponse<>(total, questionReviewItems);
    }
}
