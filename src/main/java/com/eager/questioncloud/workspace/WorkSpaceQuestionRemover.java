package com.eager.questioncloud.workspace;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionRemover {
    private final QuestionRepository questionRepository;

    public void remove(Long creatorId, Long questionId) {
        Question question = questionRepository.getForModifyAndDelete(questionId, creatorId);
        question.delete();
        questionRepository.save(question);
    }
}
