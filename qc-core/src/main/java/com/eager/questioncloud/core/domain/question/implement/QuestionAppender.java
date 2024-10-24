package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionAppender {
    private final QuestionRepository questionRepository;

    public Question append(Question question) {
        return questionRepository.save(question);
    }
}
