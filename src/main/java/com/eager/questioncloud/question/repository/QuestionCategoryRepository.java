package com.eager.questioncloud.question.repository;

import com.eager.questioncloud.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();
}
