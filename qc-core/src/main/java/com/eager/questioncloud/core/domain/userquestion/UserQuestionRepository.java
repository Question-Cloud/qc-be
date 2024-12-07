package com.eager.questioncloud.core.domain.userquestion;

import com.eager.questioncloud.core.domain.question.QuestionFilter;
import java.util.List;

public interface UserQuestionRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
