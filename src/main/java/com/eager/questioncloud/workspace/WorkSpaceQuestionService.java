package com.eager.questioncloud.workspace;

import com.eager.questioncloud.question.Question;
import com.eager.questioncloud.question.QuestionContent;
import com.eager.questioncloud.question.QuestionDto.QuestionInformation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkSpaceQuestionService {
    private final WorkSpaceQuestionRegister workSpaceQuestionRegister;
    private final WorkSpaceQuestionUpdater workSpaceQuestionUpdater;
    private final WorkSpaceQuestionRemover workSpaceQuestionRemover;
    private final WorkSpaceQuestionReader workSpaceQuestionReader;

    public int count(Long userId) {
        return workSpaceQuestionReader.count(userId);
    }

    public List<QuestionInformation> getQuestions(Long userId, Pageable pageable) {
        return workSpaceQuestionReader.getQuestions(userId, pageable);
    }

    public QuestionContent getQuestionContent(Long userId, Long questionId) {
        return workSpaceQuestionReader.getQuestionContent(userId, questionId);
    }

    public Question register(Long userId, QuestionContent questionContent) {
        return workSpaceQuestionRegister.register(userId, questionContent);
    }

    public void modify(Long userId, Long questionId, QuestionContent questionContent) {
        workSpaceQuestionUpdater.modify(userId, questionId, questionContent);
    }

    public void delete(Long userId, Long questionId) {
        workSpaceQuestionRemover.remove(userId, questionId);
    }
}
