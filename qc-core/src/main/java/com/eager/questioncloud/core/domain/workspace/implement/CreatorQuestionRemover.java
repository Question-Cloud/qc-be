package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionRemover {
    private final QuestionRepository questionRepository;

    public void delete(Question question) {
        question.delete();
        questionRepository.save(question);
    }
}
