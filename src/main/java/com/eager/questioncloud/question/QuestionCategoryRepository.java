package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionCategoryDto.QuestionCategoryItem;
import java.util.List;

public interface QuestionCategoryRepository {
    List<QuestionCategoryItem> getQuestionCategories();
}
