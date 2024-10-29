package com.eager.questioncloud.core.domain.questionhub.question.repository;

import com.eager.questioncloud.core.domain.questionhub.question.dto.QuestionCategoryDto.QuestionCategoryItem;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();
}
