package com.eager.questioncloud.core.domain.userquestion.implement;

import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;

    public void appendUserQuestion(Long userId, List<Long> questionIds) {
        userQuestionRepository.saveAll(UserQuestion.create(userId, questionIds));
    }
}
