package com.eager.questioncloud.question;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorQuestionRemover {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public void remove(Long userId, Long questionId) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = questionRepository.getForModifyAndDelete(questionId, creator.getId());
        question.delete();
        questionRepository.save(question);
    }
}
