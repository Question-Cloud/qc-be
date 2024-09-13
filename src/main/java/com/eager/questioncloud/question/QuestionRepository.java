package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import com.eager.questioncloud.question.QuestionDto.QuestionInformationForWorkSpace;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface QuestionRepository {
    int getTotalFiltering(QuestionFilter questionFilter);

    List<QuestionInformation> getQuestionListByFiltering(QuestionFilter questionFilter);

    QuestionInformation getQuestionInformation(Long questionId, Long userId);

    List<Question> getQuestionListInIds(List<Long> questionIds);

    Boolean isAvailable(Long questionId);

    Question append(Question question);

    Question getForModifyAndDelete(Long questionId, Long creatorId);

    Question get(Long questionId);

    Question save(Question question);

    List<QuestionInformationForWorkSpace> getCreatorQuestion(Long creatorId, Pageable pageable);

    int countCreatorQuestion(Long creatorId);
}
