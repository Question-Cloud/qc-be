package com.eager.questioncloud.domain.workspace.implement;

import com.eager.questioncloud.domain.question.vo.QuestionContent;
import com.eager.questioncloud.domain.question.model.Question;
import com.eager.questioncloud.domain.question.repository.QuestionRepository;
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
