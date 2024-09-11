package com.eager.questioncloud.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorQuestionService {
    private final QuestionRegister questionRegister;
    private final QuestionUpdater questionUpdater;

    public Question register(Long userId, QuestionContent questionContent) {
        return questionRegister.register(userId, questionContent);
    }

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        questionUpdater.modify(userId, questionId, questionContent);
    }
}
