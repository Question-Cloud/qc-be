package com.eager.questioncloud.workspace;

import com.eager.questioncloud.creator.Creator;
import com.eager.questioncloud.creator.CreatorReader;
import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionRemover {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public void remove(Long userId, Long questionId) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = questionRepository.getForModifyAndDelete(questionId, creator.getId());
        question.delete();
        questionRepository.save(question);
    }
}
