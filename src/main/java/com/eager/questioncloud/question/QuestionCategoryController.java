package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.question.Response.QuestionCategoriesResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/question/category")
@RequiredArgsConstructor
public class QuestionCategoryController {
    private final QuestionCategoryService questionCategoryService;

    @GetMapping
    public QuestionCategoriesResponse getQuestionCategories() {
        List<QuestionCategoryListItem> categories = questionCategoryService.getQuestionCategories();
        return new QuestionCategoriesResponse(categories);
    }
}
