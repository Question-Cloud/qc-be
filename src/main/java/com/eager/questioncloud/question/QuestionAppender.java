package com.eager.questioncloud.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionAppender {
    private final QuestionRepository questionRepository;

    public Question append(Question question) {
        return questionRepository.append(question);
    }
}
