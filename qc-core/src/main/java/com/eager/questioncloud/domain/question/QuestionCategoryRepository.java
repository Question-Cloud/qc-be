package com.eager.questioncloud.domain.question;

import com.eager.questioncloud.domain.question.QuestionCategoryGroupBySubject.MainQuestionCategory;
import java.util.List;

public interface QuestionCategoryRepository {
    List<MainQuestionCategory> getMainQuestionCategories();
}
