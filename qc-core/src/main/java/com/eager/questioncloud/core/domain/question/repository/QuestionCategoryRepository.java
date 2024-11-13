package com.eager.questioncloud.core.domain.question.repository;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import com.eager.questioncloud.core.domain.question.model.QuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();

    QuestionCategory findById(Long id);
}
