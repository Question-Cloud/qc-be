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
public class WorkSpaceQuestionUpdater {
    private final QuestionRepository questionRepository;
    private final CreatorReader creatorReader;

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        Creator creator = creatorReader.getByUserId(userId);
        Question question = questionRepository.getForModifyAndDelete(questionId, creator.getId());
        question.modify(questionContent);
        questionRepository.save(question);
    }
}
