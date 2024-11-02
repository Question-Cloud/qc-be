package com.eager.questioncloud.core.domain.feed.library.repository;

import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.feed.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;

public interface UserQuestionLibraryRepository {
    List<UserQuestion> saveAll(List<UserQuestion> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
