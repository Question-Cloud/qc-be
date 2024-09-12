package com.eager.questioncloud.workspace;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionRegister {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public Question register(Long userId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = Question.create(creator.getId(), questionContent);
        return questionRepository.append(question);
    }
}
