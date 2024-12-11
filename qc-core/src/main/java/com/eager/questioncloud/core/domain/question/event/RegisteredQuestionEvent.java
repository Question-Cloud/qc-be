package com.eager.questioncloud.core.domain.question.event;

import com.eager.questioncloud.core.domain.question.model.Question;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisteredQuestionEvent {
    private Question question;

    public static RegisteredQuestionEvent create(Question question) {
        return new RegisteredQuestionEvent(question);
    }
}
