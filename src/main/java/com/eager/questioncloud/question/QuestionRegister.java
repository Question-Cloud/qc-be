package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionRegister {
    private final QuestionAppender questionAppender;
    private final CreatorReader creatorReader;

    public Question register(Long userId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = Question.create(creator.getId(), questionContent);
        return questionAppender.append(question);
    }
}
