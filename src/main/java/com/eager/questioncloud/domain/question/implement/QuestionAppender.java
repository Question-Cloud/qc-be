package com.eager.questioncloud.domain.question.implement;

import com.eager.questioncloud.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.domain.question.model.Question;
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
