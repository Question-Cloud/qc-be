package com.eager.questioncloud.domain.library.repository;

import com.eager.questioncloud.domain.library.dto.UserQuestionDetail;
import com.eager.questioncloud.domain.library.model.UserQuestion;
import com.eager.questioncloud.domain.question.common.QuestionFilter;
import java.util.List;

public interface UserQuestionRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionDetail> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
