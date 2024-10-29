package com.eager.questioncloud.api.question;

import com.eager.questioncloud.api.question.Response.QuestionCategoriesResponse;
import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.core.domain.questionhub.question.service.QuestionCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공")
    })
    @Operation(operationId = "문제 카테고리 목록 조회", summary = "문제 카테고리 목록 조회", tags = {"question"}, description = "문제 카테고리 목록 조회")
    public QuestionCategoriesResponse getQuestionCategories() {
        List<QuestionCategoryListItem> categories = questionCategoryService.getQuestionCategories();
        return new QuestionCategoriesResponse(categories);
    }
}
