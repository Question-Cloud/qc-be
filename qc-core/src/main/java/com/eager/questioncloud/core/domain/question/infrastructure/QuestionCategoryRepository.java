package com.eager.questioncloud.core.domain.question.infrastructure;

import com.eager.questioncloud.core.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getMainQuestionCategories();
}
