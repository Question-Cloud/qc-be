package com.eager.questioncloud.workspace.implement;

import com.eager.questioncloud.question.dto.QuestionContent;
import com.eager.questioncloud.question.model.Question;
import com.eager.questioncloud.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkSpaceQuestionUpdater {
    private final QuestionRepository questionRepository;

    public void modify(Long creatorId, Long questionId, QuestionContent questionContent) {
        Question question = questionRepository.getForModifyAndDelete(questionId, creatorId);
        question.modify(questionContent);
        questionRepository.save(question);
    }
}
