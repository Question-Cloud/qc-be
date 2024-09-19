package com.eager.questioncloud.library;

import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.QuestionFilter;
import java.util.List;

public interface UserQuestionLibraryRepository {
    List<UserQuestionLibrary> saveAll(List<UserQuestionLibrary> userQuestionLibraries);

    Boolean checkDuplicate(Long userId, List<Long> questionIds);

    Boolean isOwned(Long userId, Long questionId);

    List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter);

    int countUserQuestions(QuestionFilter questionFilter);
}
