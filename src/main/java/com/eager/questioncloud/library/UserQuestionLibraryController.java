package com.eager.questioncloud.library;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.QuestionFilter;
import com.eager.questioncloud.question.QuestionLevel;
import com.eager.questioncloud.question.QuestionSortType;
import com.eager.questioncloud.question.QuestionType;
import com.eager.questioncloud.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserQuestionLibraryController {
    private final UserQuestionLibraryService userQuestionLibraryService;

    @GetMapping
    public PagingResponse<UserQuestionLibraryItem> getUserQuestionLibraryList(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestParam(required = false) List<Long> categories,
        @RequestParam(required = false) List<QuestionLevel> levels,
        @RequestParam(required = false) QuestionType questionType,
        @RequestParam(required = false) Long creatorId,
        @RequestParam QuestionSortType sort,
        Pageable pageable) {
        QuestionFilter questionFilter = new QuestionFilter(
            userPrincipal.getUser().getUid(), categories, levels, questionType, creatorId, sort, pageable);
        int total = userQuestionLibraryService.countUserQuestions(questionFilter);
        List<UserQuestionLibraryItem> userQuestions = userQuestionLibraryService.getUserQuestions(questionFilter);
        return new PagingResponse<>(total, userQuestions);
    }
}
