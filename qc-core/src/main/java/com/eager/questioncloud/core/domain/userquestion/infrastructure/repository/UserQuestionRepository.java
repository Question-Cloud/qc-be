package com.eager.questioncloud.core.domain.userquestion.infrastructure.repository;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.userquestion.dto.UserQuestionDetail;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import java.util.List;

public interface UserQuestionRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);

    void deleteAllInBatch();
}
