package com.eager.questioncloud.question;

import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import java.util.List;

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
}
