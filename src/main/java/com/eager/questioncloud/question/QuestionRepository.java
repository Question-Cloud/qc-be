package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;

public interface QuestionRepository {
    int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionType type);

    List<QuestionFilterItem> getQuestionListByFiltering(QuestionFilter questionFilter);

    QuestionDetail getQuestionDetail(Long questionId);

    List<Question> getQuestionListInIds(List<Long> questionIds);
}
