package com.eager.questioncloud.core.domain.question.repository;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.dto.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;

public interface QuestionRepository {
    int getTotalFiltering(QuestionFilter questionFilter);

    List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter);

    QuestionInformation getQuestionInformation(Long questionId, Long userId);

    List<Question> getQuestionListInIds(List<Long> questionIds);

    Boolean isAvailable(Long questionId);

    Question getForModifyAndDelete(Long questionId, Long creatorId);

    Question get(Long questionId);

    Question save(Question question);

    List<QuestionInformationForWorkSpace> getCreatorQuestion(Long creatorId, PagingInformation pagingInformation);

    int countCreatorQuestion(Long creatorId);
}
