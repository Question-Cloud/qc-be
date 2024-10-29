package com.eager.questioncloud.core.domain.questionhub.question.implement;

import com.eager.questioncloud.core.domain.questionhub.question.model.Question;
import com.eager.questioncloud.core.domain.questionhub.question.repository.QuestionRepository;
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

