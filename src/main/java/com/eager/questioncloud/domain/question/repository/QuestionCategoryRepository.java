package com.eager.questioncloud.domain.question.repository;

import com.eager.questioncloud.domain.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();
}
