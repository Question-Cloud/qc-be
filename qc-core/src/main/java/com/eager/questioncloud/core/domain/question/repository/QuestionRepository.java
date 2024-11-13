package com.eager.questioncloud.core.domain.question.repository;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;

public interface QuestionRepository {
    int countByQuestionFilter(QuestionFilter questionFilter);

    List<Question> getQuestionListInIds(List<Long> questionIds);

    Boolean isAvailable(Long questionId);

    Question findByQuestionIdAndCreatorId(Long questionId, Long creatorId);

    Question get(Long questionId);

    Question save(Question question);

    int countByCreatorId(Long creatorId);

    List<Question> getQuestionsByFilter(QuestionFilter questionFilter);
}
