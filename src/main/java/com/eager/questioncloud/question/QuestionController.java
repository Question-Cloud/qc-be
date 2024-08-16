package com.eager.questioncloud.question;

import com.eager.questioncloud.common.PagingResponse;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
        @RequestParam List<Long> categories, @RequestParam List<QuestionLevel> levels, @RequestParam QuestionSortType sort) {
        int total = questionService.getTotalFiltering(categories, levels);
        List<QuestionFilterItem> questionFilterItems = questionService.getQuestionListByFiltering(categories, levels, sort);
        return new PagingResponse<>(total, questionFilterItems);
    }
}
