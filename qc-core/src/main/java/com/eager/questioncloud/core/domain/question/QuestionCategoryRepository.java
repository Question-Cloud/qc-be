package com.eager.questioncloud.core.domain.question;

import com.eager.questioncloud.core.domain.question.QuestionCategoryGroupBySubject.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getMainQuestionCategories();
}
