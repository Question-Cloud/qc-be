package com.eager.questioncloud.core.domain.hub.question.repository;

import com.eager.questioncloud.core.domain.hub.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();
}
