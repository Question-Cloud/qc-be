package com.eager.questioncloud.domain.question.service;

import com.eager.questioncloud.domain.question.dto.QuestionCategoryDto.QuestionCategoryListItem;
import com.eager.questioncloud.domain.question.implement.QuestionCategoryReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService {
    private final QuestionCategoryReader questionCategoryReader;

    public List<QuestionCategoryListItem> getQuestionCategories() {
        return QuestionCategoryListItem.groupBy(questionCategoryReader.getQuestionCategories());
    }
}
