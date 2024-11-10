package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.hub.question.common.QuestionFilter;
import com.eager.questioncloud.core.domain.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.library.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionReader {
    private final LibraryRepository libraryRepository;

    public Boolean isOwned(Long userId, Long questionId) {
        return libraryRepository.isOwned(userId, questionId);
    }

    public Boolean isOwned(Long userId, List<Long> questionId) {
        return libraryRepository.isOwned(userId, questionId);
    }

    public List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter) {
        return libraryRepository.getUserQuestions(questionFilter);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return libraryRepository.countUserQuestions(questionFilter);
    }
}
