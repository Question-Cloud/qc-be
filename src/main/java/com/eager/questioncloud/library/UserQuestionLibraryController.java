package com.eager.questioncloud.library;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class UserQuestionLibraryController {
    private final UserQuestionLibraryService userQuestionLibraryService;

    @GetMapping
    public PagingResponse<UserQuestionLibraryItem> getUserQuestionLibraryList(QuestionFilter questionFilter) {
        int total = userQuestionLibraryService.countUserQuestions(questionFilter);
        List<UserQuestionLibraryItem> userQuestions = userQuestionLibraryService.getUserQuestions(questionFilter);
        return new PagingResponse<>(total, userQuestions);
    }
}
