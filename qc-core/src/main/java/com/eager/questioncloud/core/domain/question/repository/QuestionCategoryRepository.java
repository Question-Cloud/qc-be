package com.eager.questioncloud.core.domain.question.repository;

import com.eager.questioncloud.core.domain.question.dto.MainQuestionCategoryList.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getQuestionCategories();
}
