package com.eager.questioncloud.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionRemover {
    private final QuestionRepository questionRepository;

    public void remove(Long creatorId, Long questionId) {
        Question question = questionRepository.getForModifyAndDelete(questionId, creatorId);
        question.delete();
        questionRepository.save(question);
    }
}
