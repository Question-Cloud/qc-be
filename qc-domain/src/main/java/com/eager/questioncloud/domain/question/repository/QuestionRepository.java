package com.eager.questioncloud.domain.question.repository;

import com.eager.questioncloud.common.PagingInformation;
import com.eager.questioncloud.domain.question.common.QuestionFilter;
import com.eager.questioncloud.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.domain.question.model.Question;
import java.util.List;

public interface QuestionRepository {
    int countByQuestionFilter(QuestionFilter questionFilter);

    List<QuestionInformation> getQuestionInformation(QuestionFilter questionFilter);

    QuestionInformation getQuestionInformation(Long questionId, Long userId);

    List<Question> getQuestionsByQuestionIds(List<Long> questionIds);

    Boolean isAvailable(Long questionId);

    Question findByQuestionIdAndCreatorId(Long questionId, Long creatorId);

    Question get(Long questionId);

    Question save(Question question);

    List<QuestionInformation> findByCreatorIdWithPaging(Long creatorId, PagingInformation pagingInformation);

    int countByCreatorId(Long creatorId);
}
