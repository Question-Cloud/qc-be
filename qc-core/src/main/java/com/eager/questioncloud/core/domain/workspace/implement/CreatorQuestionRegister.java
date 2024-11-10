package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatorQuestionRegister {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question register(Question question) {
        return questionRepository.save(question);
    }
}
