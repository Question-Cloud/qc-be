package com.eager.questioncloud.core.domain.question;

import com.eager.questioncloud.core.common.PagingInformation;
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

    void deleteAllInBatch();
}
