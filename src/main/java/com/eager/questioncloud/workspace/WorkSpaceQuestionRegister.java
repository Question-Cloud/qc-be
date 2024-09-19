package com.eager.questioncloud.workspace;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionRegister {
    private final QuestionRepository questionRepository;

    public Question register(Long creatorId, QuestionContent questionContent) {
        Question question = Question.create(creatorId, questionContent);
        return questionRepository.save(question);
    }
}
