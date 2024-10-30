package com.eager.questioncloud.core.domain.hub.question.implement;

import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.hub.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionRemover {
    private final QuestionRepository questionRepository;

    public void delete(Question question) {
        question.delete();
        questionRepository.save(question);
    }
}

