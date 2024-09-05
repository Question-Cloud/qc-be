package com.eager.questioncloud.question;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.Response.QuestionInformationResponse;
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
    public PagingResponse<QuestionInformation> getQuestionListByFiltering(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(required = false) List<Long> categories,
        @RequestParam(required = false) List<QuestionLevel> levels,
        @RequestParam(required = false) QuestionType questionType,
        @RequestParam(required = false) Long creatorId,
        @RequestParam QuestionSortType sort,
        Pageable pageable) {
        QuestionFilter questionFilter = new QuestionFilter(
            userPrincipal.getUser().getUid(), categories, levels, questionType, creatorId, sort, pageable);
        int total = questionService.getTotalFiltering(questionFilter);
        List<QuestionInformation> questionInformation = questionService.getQuestionListByFiltering(questionFilter);
        return new PagingResponse<>(total, questionInformation);
    }

    @GetMapping("/{questionId}")
    public QuestionInformationResponse getQuestionDetail(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable Long questionId) {
        QuestionInformation questionInformation = questionService.getQuestionInformation(questionId, userPrincipal.getUser().getUid());
        return new QuestionInformationResponse(questionInformation);
    }
}
