package com.eager.questioncloud.workspace.implement;

import com.eager.questioncloud.question.domain.Question;
import com.eager.questioncloud.question.repository.QuestionRepository;
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
