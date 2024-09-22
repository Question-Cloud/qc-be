package com.eager.questioncloud.workspace;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionRepository;
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