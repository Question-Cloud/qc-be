package com.eager.questioncloud.core.domain.feed.library.repository;

import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.feed.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;

public interface LibraryRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
