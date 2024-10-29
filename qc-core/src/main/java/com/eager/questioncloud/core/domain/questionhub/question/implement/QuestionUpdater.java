package com.eager.questioncloud.core.domain.questionhub.question.implement;

import com.eager.questioncloud.core.domain.questionhub.question.model.Question;
import com.eager.questioncloud.core.domain.questionhub.question.repository.QuestionRepository;
import com.eager.questioncloud.core.domain.questionhub.question.vo.QuestionContent;
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
