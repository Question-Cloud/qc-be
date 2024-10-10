package com.eager.questioncloud.library;

import com.eager.questioncloud.library.UserQuestionLibraryDto.UserQuestionLibraryItem;
import com.eager.questioncloud.question.dto.QuestionFilter;
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

    public List<UserQuestionLibraryItem> getUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionLibraryRepository.countUserQuestions(questionFilter);
    }
}
