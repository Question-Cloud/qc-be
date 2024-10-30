package com.eager.questioncloud.core.domain.hub.question.implement;

import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.hub.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.hub.question.vo.QuestionContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionUpdater {
    private final QuestionRepository questionRepository;

    public void modifyQuestionContent(Question question, QuestionContent questionContent) {
        question.modify(questionContent);
        questionRepository.save(question);
    }
}
