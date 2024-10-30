package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.dto.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionLibraryRepository;
import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionLibraryReader {
    private final UserQuestionLibraryRepository userQuestionLibraryRepository;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionLibraryRepository.isOwned(userId, questionId);
    }

    public Boolean isOwned(Long userId, List<Long> questionId) {
        return userQuestionLibraryRepository.isOwned(userId, questionId);
    }

    public List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryRepository.countUserQuestions(questionFilter);
    }
}
