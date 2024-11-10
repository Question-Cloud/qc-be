package com.eager.questioncloud.core.domain.question.implement;

import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.question.vo.QuestionContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionRegister {
    private final QuestionRepository questionRepository;

    @Transactional
    public Question register(Long creatorId, QuestionContent questionContent) {
        Question question = questionRepository.save(Question.create(creatorId, questionContent));
        return question;
    }
}
