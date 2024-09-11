package com.eager.questioncloud.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionRegister {
    private final QuestionAppender questionAppender;

    public Question register(Long creatorId, QuestionContent questionContent) {
        Question question = Question.create(creatorId, questionContent);
        return questionAppender.append(question);
    }
}
