package com.eager.questioncloud.core.domain.library.repository;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import java.util.List;
import java.util.Set;

public interface UserQuestionRepository {
    void saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestion> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);

    Set<Long> checkIsOwned(Long userId, List<Long> questionIds);
}
