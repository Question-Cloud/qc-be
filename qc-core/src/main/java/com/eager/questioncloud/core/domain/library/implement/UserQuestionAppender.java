package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;

    public List<UserQuestion> appendUserQuestions(Long userId, List<Long> questionIds) {
        return userQuestionRepository.saveAll(UserQuestion.create(userId, questionIds));
    }
}
