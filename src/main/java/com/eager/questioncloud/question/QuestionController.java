package com.eager.questioncloud.question;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import com.eager.questioncloud.question.Response.QuestionDetailResponse;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public PagingResponse<QuestionFilterItem> getQuestionListByFiltering(
        @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam List<Long> categories, @RequestParam List<QuestionLevel> levels,
        @RequestParam QuestionType questionType, @RequestParam Long creatorId, @RequestParam QuestionSortType sort, Pageable pageable) {
        QuestionFilter questionFilter = new QuestionFilter(
            userPrincipal.getUser().getUid(), categories, levels, questionType, creatorId, sort, pageable);
        int total = questionService.getTotalFiltering(questionFilter);
        List<QuestionFilterItem> questionFilterItems = questionService.getQuestionListByFiltering(questionFilter);
        return new PagingResponse<>(total, questionFilterItems);
    }

    @GetMapping("/{questionId}")
    public QuestionDetailResponse getQuestionDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionDetail questionDetail = questionService.getQuestionDetail(questionId, userPrincipal.getUser().getUid());
        return new QuestionDetailResponse(questionDetail);
    }
}
