package com.eager.questioncloud.core.domain.library.repository;

import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import java.util.List;

public interface LibraryRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}