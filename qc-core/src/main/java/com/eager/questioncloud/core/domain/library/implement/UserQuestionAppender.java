package com.eager.questioncloud.core.domain.library.implement;

import com.eager.questioncloud.core.domain.library.model.UserQuestion;
import com.eager.questioncloud.core.domain.library.repository.UserQuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQuestionAppender {
    private final UserQuestionRepository userQuestionRepository;

    public void appendUserQuestions(Long userId, List<Question> questions) {
        userQuestionRepository.saveAll(UserQuestion.create(userId, questions));
    }
}
