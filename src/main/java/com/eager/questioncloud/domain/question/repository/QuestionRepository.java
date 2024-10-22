package com.eager.questioncloud.domain.question.repository;

import com.eager.questioncloud.domain.question.common.QuestionFilter;
import com.eager.questioncloud.domain.question.dto.QuestionDto.QuestionInformation;
import com.eager.questioncloud.domain.question.dto.QuestionDto.QuestionInformationForWorkSpace;
import com.eager.questioncloud.domain.question.model.Question;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionRepository {
    int getTotalFiltering(QuestionFilter questionFilter);

    List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter);

    QuestionInformation getQuestionInformation(Long questionId, Long userId);

    List<Question> getQuestionListInIds(List<Long> questionIds);

    Boolean isAvailable(Long questionId);

    Question getForModifyAndDelete(Long questionId, Long creatorId);

    Question get(Long questionId);

    Question save(Question question);

    List<QuestionInformationForWorkSpace> getCreatorQuestion(Long creatorId, Pageable pageable);

    int countCreatorQuestion(Long creatorId);
}
