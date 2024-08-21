package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionDetail;
import com.eager.questioncloud.question.QuestionDto.QuestionFilterItem;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionRepository {
    int getTotalFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionType type);

    List<QuestionFilterItem> getQuestionListByFiltering(List<Long> questionCategoryIds, List<QuestionLevel> questionLevels, QuestionType questionType,
        QuestionSortType sort, Pageable pageable);

    QuestionDetail getQuestionDetail(Long questionId);

    List<Question> getQuestionListInIds(List<Long> questionIds);
}
