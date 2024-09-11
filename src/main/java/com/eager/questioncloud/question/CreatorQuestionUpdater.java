package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionUpdater {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = questionRepository.getForModifyAndDelete(questionId, creator.getId());
        question.modify(questionContent);
        questionRepository.save(question);
    }
}
