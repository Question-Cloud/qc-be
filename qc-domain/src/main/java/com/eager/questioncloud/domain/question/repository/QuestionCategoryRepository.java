package com.eager.questioncloud.domain.question.repository;

import com.eager.questioncloud.domain.question.dto.QuestionCategoryGroupBySubject.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getMainQuestionCategories();
}
