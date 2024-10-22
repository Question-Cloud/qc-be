package com.eager.questioncloud.domain.workspace.implement;

import com.eager.questioncloud.domain.question.model.Question;
import com.eager.questioncloud.domain.question.repository.QuestionRepository;
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
