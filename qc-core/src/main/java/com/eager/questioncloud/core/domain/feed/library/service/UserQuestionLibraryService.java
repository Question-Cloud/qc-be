package com.eager.questioncloud.core.domain.feed.library.service;

import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionLibraryReader;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQuestionLibraryService {
    private final UserQuestionLibraryReader userQuestionLibraryReader;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionLibraryReader.isOwned(userId, questionId);
    }

    public List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryReader.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryReader.countUserQuestions(questionFilter);
    }
}
