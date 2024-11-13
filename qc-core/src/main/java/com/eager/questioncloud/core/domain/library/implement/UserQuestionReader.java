package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.dto.UserQuestionDto.UserQuestionItem;
import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.question.common.QuestionFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionReader {
    private final UserQuestionRepository userQuestionRepository;

    public Boolean isOwned(Long userId, Long questionId) {
        return userQuestionRepository.isOwned(userId, questionId);
    }

    public Boolean isOwned(Long userId, List<Long> questionId) {
        return userQuestionRepository.isOwned(userId, questionId);
    }

    public List<UserQuestionItem> getUserQuestions(QuestionFilter questionFilter) {
        List<UserQuestion> userQuestions = userQuestionRepository.getUserQuestions(questionFilter);
        return UserQuestionItem.from(userQuestions);
    }

    public int countUserQuestions(QuestionFilter questionFilter) {
        return userQuestionRepository.countUserQuestions(questionFilter);
    }
}
