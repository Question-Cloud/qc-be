package com.eager.questioncloud.core.domain.question.repository;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryDto.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getQuestionCategories();
}
