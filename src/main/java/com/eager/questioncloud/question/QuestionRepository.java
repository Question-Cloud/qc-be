package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;

public interface QuestionRepository {
    int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels);

    List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionSortType sort);

    QuestionDetail getQuestionDetail(Long questionId);
}
