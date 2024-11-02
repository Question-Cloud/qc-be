package com.eager.questioncloud.core.domain.feed.library.service;

import com.eager.questioncloud.core.domain.feed.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQuestionLibraryService {
    private final UserQuestionReader userQuestionReader;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionReader.isOwned(userId, questionId);
    }

    public List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionReader.countUserQuestions(questionFilter);
    }
}
