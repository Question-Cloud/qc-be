package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionRegister {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public Question register(Long userId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = Question.create(creator.getId(), questionContent);
        return questionRepository.append(question);
    }
}
