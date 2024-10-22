package com.eager.questioncloud.domain.library.repository;

import com.eager.questioncloud.domain.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.domain.library.model.UserQuestionLibrary;
import com.eager.questioncloud.domain.question.common.QuestionFilter;
import java.util.List;

public interface UserQuestionLibraryRepository {
    List<UserQuestionLibrary> saveAll(List<UserQuestionLibrary> userQuestionLibraries);

    Boolean isOwned(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
