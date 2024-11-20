package com.eager.questioncloud.domain.question;

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
